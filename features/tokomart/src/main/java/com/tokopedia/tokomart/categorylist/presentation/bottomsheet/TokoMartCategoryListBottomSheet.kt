package com.tokopedia.tokomart.categorylist.presentation.bottomsheet

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
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.di.component.DaggerTokoMartCategoryListComponent
import com.tokopedia.tokomart.categorylist.domain.mapper.CategoryListMapper
import com.tokopedia.tokomart.categorylist.presentation.activity.TokoMartCategoryListActivity.Companion.PARAM_WAREHOUSE_ID
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokomart.categorylist.presentation.view.TokoNowCategoryList
import com.tokopedia.tokomart.categorylist.presentation.viewholder.CategoryListItemViewHolder.CategoryListListener
import com.tokopedia.tokomart.categorylist.presentation.viewmodel.TokoMartCategoryListViewModel
import com.tokopedia.tokomart.categorylist.presentation.viewmodel.TokoMartCategoryListViewModel.Companion.ERROR_MAINTENANCE
import com.tokopedia.tokomart.categorylist.presentation.viewmodel.TokoMartCategoryListViewModel.Companion.ERROR_PAGE_FULL
import com.tokopedia.tokomart.categorylist.presentation.viewmodel.TokoMartCategoryListViewModel.Companion.ERROR_PAGE_NOT_FOUND
import com.tokopedia.tokomart.categorylist.presentation.viewmodel.TokoMartCategoryListViewModel.Companion.ERROR_SERVER
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.UnknownHostException
import javax.inject.Inject

class TokoMartCategoryListBottomSheet : BottomSheetUnify(), CategoryListListener {

    companion object {
        private val TAG = TokoMartCategoryListBottomSheet::class.simpleName

        fun newInstance(warehouseId: String): TokoMartCategoryListBottomSheet {
            return TokoMartCategoryListBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PARAM_WAREHOUSE_ID, warehouseId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: TokoMartCategoryListViewModel

    private var accordionCategoryList: AccordionUnify? = null
    private var loader: LoaderUnify? = null
    private var menuTitle: String = ""
    private var globalError: GlobalError? = null

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
        super.onDismiss(dialog)
        activity?.finish()
    }

    override fun onClickCategory() {
        dismiss()
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initInjector() {
        DaggerTokoMartCategoryListComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(R.layout.bottomsheet_tokomart_category_list, container)
        val menuTitle = itemView.context.getString(R.string.tokomart_category_list_bottom_sheet_title)
        accordionCategoryList = itemView.findViewById(R.id.accordion_category_list)
        loader = itemView.findViewById(R.id.loader)
        globalError = itemView.findViewById(R.id.layout_global_error_category_list)
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
        categoryList.forEach { category ->
            val accordionDataUnify = AccordionDataUnify(
                title = category.name,
                iconUrl = category.imageUrl,
                expandableView = createCategoryList(category)
            )
            addCategoryGroup(accordionDataUnify)
        }
        hideLoader()
    }

    private fun addCategoryGroup(accordionDataUnify: AccordionDataUnify) {
        accordionCategoryList?.addGroup(accordionDataUnify)?.accordionIcon?.apply {
            val iconSize = context?.resources
                ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl5).orZero()
            layoutParams.height = iconSize
            layoutParams.width = iconSize
        }
    }

    private fun createCategoryList(category: CategoryListItemUiModel): TokoNowCategoryList {
        return TokoNowCategoryList(requireContext(), this).apply {
            val paddingLeft = context?.resources
                ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()
            setPadding(paddingLeft, 0, 0, 0)
            setupCategoryList(category.childList)
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
    }

    private fun showGlobalErrorPageNotFound(){
        globalError?.let {
            it.errorIllustration.setImage(context?.getString(R.string.tokomart_category_list_bottom_sheet_error_not_found_image_url)?: "", 0f)
            it.errorTitle.text = context?.getString(R.string.tokomart_category_list_bottom_sheet_error_not_found_title)?: ""
            it.errorDescription.text = context?.getString(R.string.tokomart_category_list_bottom_sheet_error_not_found_desc)?: ""
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
        accordionCategoryList?.show()
    }

    private fun showLoader() {
        loader?.show()
        accordionCategoryList?.hide()
        globalError?.hide()
    }

    private fun showError(){
        loader?.hide()
        accordionCategoryList?.hide()
        globalError?.show()
    }
}