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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.di.component.DaggerTokoMartCategoryListComponent
import com.tokopedia.tokomart.categorylist.presentation.activity.TokoMartCategoryListActivity.Companion.PARAM_WAREHOUSE_ID
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokomart.categorylist.presentation.view.TokoNowCategoryList
import com.tokopedia.tokomart.categorylist.presentation.viewholder.CategoryListItemViewHolder.*
import com.tokopedia.tokomart.categorylist.presentation.viewmodel.TokoMartCategoryListViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.usecase.coroutines.Success
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
        clearContentPadding = true
        isFullpage = true
        setTitle(menuTitle)
        setChild(itemView)
    }

    private fun observeLiveData() {
        observe(viewModel.categoryList) {
            if (it is Success) {
                showCategoryList(it.data)
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

    private fun hideLoader() {
        loader?.hide()
        accordionCategoryList?.show()
    }

    private fun showLoader() {
        loader?.show()
        accordionCategoryList?.hide()
    }
}