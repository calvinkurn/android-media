package com.tokopedia.tokopedianow.categorylist.presentation.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.GlobalError.Companion.MAINTENANCE
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_FULL
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics
import com.tokopedia.tokopedianow.categorylist.di.component.DaggerCategoryListComponent
import com.tokopedia.tokopedianow.categorylist.domain.mapper.CategoryListMapper
import com.tokopedia.tokopedianow.categorylist.presentation.activity.CategoryListActivity.Companion.PARAM_WAREHOUSE_ID
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokopedianow.categorylist.presentation.view.CategoryListView
import com.tokopedia.tokopedianow.categorylist.presentation.view.CategoryListView.CategoryListListener
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.CategoryListViewModel
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.CategoryListViewModel.Companion.ERROR_MAINTENANCE
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.CategoryListViewModel.Companion.ERROR_PAGE_FULL
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.CategoryListViewModel.Companion.ERROR_PAGE_NOT_FOUND
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.CategoryListViewModel.Companion.ERROR_SERVER
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.UnknownHostException
import javax.inject.Inject

class CategoryListBottomSheet : BottomSheetUnify() {

    companion object {
        private const val ERROR_STATE_NOT_FOUND_IMAGE_URL = "https://images.tokopedia.net/img/error_page_400_category_list.png"
        private val TAG = CategoryListBottomSheet::class.simpleName

        fun newInstance(warehouseId: String): CategoryListBottomSheet {
            return CategoryListBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PARAM_WAREHOUSE_ID, warehouseId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: CategoryListViewModel

    @Inject
    lateinit var analytics: CategoryListAnalytics

    private var accordionCategoryList: AccordionUnify? = null
    private var loader: View? = null
    private var menuTitle: String = ""
    private var globalError: GlobalError? = null
    private var contentContainer: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        getCategoryList()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onDismiss(dialog: DialogInterface) {
        analytics.onClickCloseButton()
        super.onDismiss(dialog)
        activity?.finish()
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initInjector() {
        DaggerCategoryListComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(R.layout.bottomsheet_tokomart_category_list, container)
        menuTitle = itemView.context.getString(R.string.tokomart_category_list_bottom_sheet_title)
        accordionCategoryList = itemView.findViewById(R.id.accordion_category_list)
        loader = itemView.findViewById(R.id.loader)
        globalError = itemView.findViewById(R.id.layout_global_error_category_list)
        contentContainer = itemView.findViewById(R.id.content_container)
        clearContentPadding = true
        isFullpage = true
        setTitle(menuTitle)
        setChild(itemView)
    }

    private fun observeLiveData() {
        observe(viewModel.categoryList) {
            if (it is Success) {
                if(it.data.header.errorCode.isNullOrEmpty()){
                    showCategoryList(CategoryListMapper.mapToUiModel(it.data.data))
                } else {
                    showGlobalError(null, it.data.header.errorCode)
                }
            } else if(it is Fail) {
                showGlobalError(it.throwable)
            }
        }
    }

    private fun getCategoryList() {
        val warehouseId = arguments?.getString(PARAM_WAREHOUSE_ID).orEmpty()
        viewModel.getCategoryList(warehouseId)
        showLoader()
    }

    private fun showCategoryList(categoryList: List<CategoryListItemUiModel>) {
        setTitle(menuTitle)
        categoryList.forEach { category ->
            val accordionDataUnify = AccordionDataUnify(
                title = category.name,
                iconUrl = category.imageUrl,
                expandableView = createCategoryList(category)
            )
            addCategoryGroup(accordionDataUnify, categoryList)
        }
        hideLoader()
    }

    private fun addCategoryGroup(accordionDataUnify: AccordionDataUnify, categoryList: List<CategoryListItemUiModel>) {
        accordionCategoryList?.addGroup(accordionDataUnify)?.accordionIcon?.apply {
            val iconSize = context?.resources
                ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl5).orZero()
            layoutParams.height = iconSize
            layoutParams.width = iconSize
        }
        accordionCategoryList?.onItemClick = { position, isExpanded ->
            if (isExpanded) {
                analytics.onExpandLeveOneCategory(categoryList[position].id)
            } else {
                analytics.onCollapseLevelOneCategory(categoryList[position].id)
            }
        }
    }

    private fun createCategoryList(category: CategoryListItemUiModel): CategoryListView {
        return CategoryListView(requireContext(), analytics, object: CategoryListListener {
            override fun onClickCategoryItem() = dismiss()
        }).apply {
            val paddingLeft = context?.resources
                ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()
            setPadding(paddingLeft, 0, 0, 0)
            setup(category)
        }
    }

    private fun showGlobalError(throwable: Throwable?, errorCode: String = ""){
        showError()
        val typeError = when{
            throwable is UnknownHostException -> NO_CONNECTION
            errorCode == ERROR_PAGE_FULL -> PAGE_FULL
            errorCode == ERROR_SERVER -> SERVER_ERROR
            errorCode == ERROR_MAINTENANCE -> MAINTENANCE
            else -> SERVER_ERROR
        }

        globalError?.setType(typeError)
        if(errorCode == ERROR_PAGE_NOT_FOUND){
            showGlobalErrorPageNotFound()
        } else if (errorCode == ERROR_SERVER || errorCode == ERROR_MAINTENANCE){
            globalError?.let {
                it.errorAction.show()
                it.errorAction.text = context?.getString(R.string.tokomart_category_list_bottom_sheet_error_go_to_homepage)
                it.setActionClickListener {
                    dismiss()
                }
            }
        } else if(typeError == NO_CONNECTION){
            globalError?.setButtonFull(true)
        } else {
            actionClickListenerTryAgain()
        }
        setTitle("")
    }

    private fun showGlobalErrorPageNotFound(){
        globalError?.let {
            it.errorIllustration.setImage(ERROR_STATE_NOT_FOUND_IMAGE_URL, 0f)
            it.errorTitle.text = context?.getString(R.string.tokomart_category_list_bottom_sheet_error_not_found_title).orEmpty()
            it.errorDescription.text = context?.getString(R.string.tokomart_category_list_bottom_sheet_error_not_found_desc).orEmpty()
        }
        actionClickListenerTryAgain()
    }

    private fun actionClickListenerTryAgain(){
        globalError?.let {
            it.setActionClickListener {
                getCategoryList()
            }
        }
    }

    private fun hideLoader() {
        loader?.hide()
        globalError?.hide()
        contentContainer?.show()
    }

    private fun showLoader() {
        contentContainer?.hide()
        globalError?.hide()
        loader?.show()
    }

    private fun showError(){
        loader?.hide()
        contentContainer?.hide()
        globalError?.show()
    }
}