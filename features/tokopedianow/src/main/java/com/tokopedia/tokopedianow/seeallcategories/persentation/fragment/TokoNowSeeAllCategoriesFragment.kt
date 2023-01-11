package com.tokopedia.tokopedianow.seeallcategories.persentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.seeallcategories.persentation.viewmodel.TokoNowSeeAllCategoriesViewModel
import com.tokopedia.tokopedianow.categorylist.presentation.activity.TokoNowCategoryListActivity
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.TokoNowCategoryListViewModel
import com.tokopedia.tokopedianow.common.domain.mapper.CategoryListMapper.mapToSeeAllCategoriesItemUiModel
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.seeallcategories.di.component.DaggerSeeAllCategoriesComponent
import com.tokopedia.tokopedianow.seeallcategories.persentation.adapter.SeeAllCategoriesAdapter
import com.tokopedia.tokopedianow.seeallcategories.persentation.adapter.SeeAllCategoriesAdapterTypeFactory
import com.tokopedia.tokopedianow.seeallcategories.persentation.decoration.SeeAllCategoriesDecoration
import com.tokopedia.tokopedianow.seeallcategories.persentation.uimodel.SeeAllCategoriesItemUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryMenuBinding
import com.tokopedia.tokopedianow.seeallcategories.persentation.viewholder.SeeAllCategoriesItemViewHolder
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.UnknownHostException
import javax.inject.Inject

class TokoNowSeeAllCategoriesFragment: Fragment(), SeeAllCategoriesItemViewHolder.SeeAllCategoriesListener {

    companion object {
        private const val ERROR_STATE_NOT_FOUND_IMAGE_URL = "https://images.tokopedia.net/img/error_page_400_category_list.png"
        private const val SPAN_COUNT = 3

        fun newInstance(warehouseId: String): TokoNowSeeAllCategoriesFragment {
            return TokoNowSeeAllCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(TokoNowCategoryListActivity.PARAM_WAREHOUSE_ID, warehouseId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelTokoNow: TokoNowSeeAllCategoriesViewModel

    private var binding by autoClearedNullable<FragmentTokopedianowCategoryMenuBinding>()
    private var adapter by autoClearedNullable<SeeAllCategoriesAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTokopedianowCategoryMenuBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        observeLiveData()
        getCategoryList()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onCategoryClicked(data: SeeAllCategoriesItemUiModel) {
        RouteManager.route(context, data.appLink)
    }

    private fun FragmentTokopedianowCategoryMenuBinding.setupHeader() {
        huCategoryMenu.apply {
            isShowShadow = false
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun FragmentTokopedianowCategoryMenuBinding.setupRecyclerView() {
        adapter = SeeAllCategoriesAdapter(SeeAllCategoriesAdapterTypeFactory(this@TokoNowSeeAllCategoriesFragment))
        rvCategoryMenu.apply {
            addItemDecoration(SeeAllCategoriesDecoration(getDpFromDimen(context, com.tokopedia.unifyprinciples.R.dimen.unify_space_16).toIntSafely()))
            adapter = this@TokoNowSeeAllCategoriesFragment.adapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
    }

    private fun FragmentTokopedianowCategoryMenuBinding.setupGlobalError(
        throwable: Throwable?,
        errorCode: String
    ) {
        val errorType = getGlobalErrorType(
            throwable = throwable,
            errorCode = errorCode
        )
        errorState.setType(errorType)

        if(errorCode == TokoNowCategoryListViewModel.ERROR_PAGE_NOT_FOUND){
            setupGlobalErrorPageNotFound()
        } else if (errorCode == TokoNowCategoryListViewModel.ERROR_SERVER || errorCode == TokoNowCategoryListViewModel.ERROR_MAINTENANCE){
            setupGlobalErrorServerMaintenance()
        } else if(errorType == GlobalError.NO_CONNECTION){
            setupGlobalErrorNoConnection()
        } else {
            setActionClickTryAgainListener()
        }
    }

    private fun FragmentTokopedianowCategoryMenuBinding.setupGlobalErrorPageNotFound(){
        errorState.apply {
            errorIllustration.setImage(ERROR_STATE_NOT_FOUND_IMAGE_URL, 0f)
            errorTitle.text = context?.getString(R.string.tokopedianow_category_list_bottom_sheet_error_not_found_title).orEmpty()
            errorDescription.text = context?.getString(R.string.tokopedianow_category_list_bottom_sheet_error_not_found_desc).orEmpty()
        }
        setActionClickTryAgainListener()
    }

    private fun FragmentTokopedianowCategoryMenuBinding.setupGlobalErrorServerMaintenance() {
        errorState.let {
            it.errorAction.show()
            it.errorAction.text = context?.getString(R.string.tokopedianow_category_list_bottom_sheet_error_go_to_homepage)
            it.setActionClickListener {
                onBackPressed()
            }
        }
    }

    private fun FragmentTokopedianowCategoryMenuBinding.setupGlobalErrorNoConnection() {
        errorState.setButtonFull(true)
        setActionClickTryAgainListener()
    }

    private fun FragmentTokopedianowCategoryMenuBinding.setActionClickTryAgainListener(){
        errorState.setActionClickListener {
            getCategoryList()
        }
    }

    private fun initInjector() {
        DaggerSeeAllCategoriesComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupUi() {
        binding?.apply {
            showLoader()
            setupHeader()
            setupRecyclerView()
        }
    }

    private fun observeLiveData() {
        binding?.apply {
            observe(viewModelTokoNow.categoryList) {
                when(it) {
                    is Success -> onSuccessGetCategoryListResponse(it)
                    is Fail -> onFailGetCategoryListResponse(it)
                }
            }
        }
    }

    private fun FragmentTokopedianowCategoryMenuBinding.onSuccessGetCategoryListResponse(result : Success<GetCategoryListResponse.CategoryListResponse>) {
        hideLoader()
        if(result.data.header.errorCode.isNullOrEmpty()){
            adapter?.submitList(result.data.mapToSeeAllCategoriesItemUiModel())
        } else {
            showGlobalError(
                throwable = null,
                errorCode = result.data.header.errorCode
            )
        }
    }

    private fun FragmentTokopedianowCategoryMenuBinding.onFailGetCategoryListResponse(result: Fail) {
        showGlobalError(result.throwable)
    }

    private fun FragmentTokopedianowCategoryMenuBinding.showGlobalError(throwable: Throwable?, errorCode: String = ""){
        showError()
        setupGlobalError(
            throwable = throwable,
            errorCode = errorCode
        )
    }

    private fun getGlobalErrorType(throwable: Throwable?, errorCode: String = ""): Int {
        return when{
            throwable is UnknownHostException -> GlobalError.NO_CONNECTION
            errorCode == TokoNowCategoryListViewModel.ERROR_PAGE_FULL -> GlobalError.PAGE_FULL
            errorCode == TokoNowCategoryListViewModel.ERROR_SERVER -> GlobalError.SERVER_ERROR
            errorCode == TokoNowCategoryListViewModel.ERROR_MAINTENANCE -> GlobalError.MAINTENANCE
            else -> GlobalError.SERVER_ERROR
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
    }

    private fun getCategoryList() {
        val warehouseId = arguments?.getString(TokoNowCategoryListActivity.PARAM_WAREHOUSE_ID).orEmpty()
        viewModelTokoNow.getCategoryList(warehouseId)
    }

    private fun FragmentTokopedianowCategoryMenuBinding.hideLoader() {
        loadingState.root.hide()
        errorState.hide()
        rvCategoryMenu.show()
    }

    private fun FragmentTokopedianowCategoryMenuBinding.showLoader() {
        loadingState.root.show()
        rvCategoryMenu.hide()
        errorState.hide()
    }

    private fun FragmentTokopedianowCategoryMenuBinding.showError(){
        rvCategoryMenu.hide()
        errorState.show()
    }

}
