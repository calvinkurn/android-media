package com.tokopedia.tokopedianow.seeallcategory.persentation.fragment

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
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.TokoNowCategoryListViewModel
import com.tokopedia.tokopedianow.common.domain.mapper.CategoryListMapper.mapToSeeAllCategoryItemUiModel
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowSeeAllCategoryBinding
import com.tokopedia.tokopedianow.seeallcategory.analytic.SeeAllCategoryAnalytics
import com.tokopedia.tokopedianow.seeallcategory.di.component.DaggerSeeAllCategoryComponent
import com.tokopedia.tokopedianow.seeallcategory.persentation.adapter.SeeAllCategoryAdapter
import com.tokopedia.tokopedianow.seeallcategory.persentation.adapter.SeeAllCategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.seeallcategory.persentation.decoration.SeeAllCategoryDecoration
import com.tokopedia.tokopedianow.seeallcategory.persentation.uimodel.SeeAllCategoryItemUiModel
import com.tokopedia.tokopedianow.seeallcategory.persentation.viewholder.SeeAllCategoryItemViewHolder
import com.tokopedia.tokopedianow.seeallcategory.persentation.viewmodel.TokoNowSeeAllCategoryViewModel
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.UnknownHostException
import javax.inject.Inject

class TokoNowSeeAllCategoryFragment : Fragment(), SeeAllCategoryItemViewHolder.SeeAllCategoryListener {

    companion object {
        private const val ERROR_STATE_NOT_FOUND_IMAGE_URL = TokopediaImageUrl.ERROR_STATE_NOT_FOUND_IMAGE_URL
        private const val SPAN_COUNT = 3

        fun newInstance(): TokoNowSeeAllCategoryFragment {
            return TokoNowSeeAllCategoryFragment()
        }
    }

    @Inject
    lateinit var viewModelTokoNow: TokoNowSeeAllCategoryViewModel

    @Inject
    lateinit var analytics: SeeAllCategoryAnalytics

    private var binding by autoClearedNullable<FragmentTokopedianowSeeAllCategoryBinding>()
    private var adapter by autoClearedNullable<SeeAllCategoryAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTokopedianowSeeAllCategoryBinding.inflate(inflater, container, false)
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

    override fun onCategoryClicked(data: SeeAllCategoryItemUiModel, position: Int) {
        RouteManager.route(context, data.appLink)

        analytics.onCategoryClicked(
            categoryId = data.id,
            categoryName = data.name,
            warehouseId = getWarehouseId(),
            position = position
        )
    }

    override fun onCategoryImpressed(data: SeeAllCategoryItemUiModel, position: Int) {
        analytics.onCategoryImpressed(
            categoryId = data.id,
            categoryName = data.name,
            warehouseId = getWarehouseId(),
            position = position
        )
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.setupHeader() {
        huCategoryMenu.apply {
            isShowShadow = false
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.setupRecyclerView() {
        adapter = SeeAllCategoryAdapter(SeeAllCategoryAdapterTypeFactory(this@TokoNowSeeAllCategoryFragment))
        rvCategoryMenu.apply {
            addItemDecoration(SeeAllCategoryDecoration(getDpFromDimen(context, com.tokopedia.unifyprinciples.R.dimen.unify_space_16).toIntSafely()))
            adapter = this@TokoNowSeeAllCategoryFragment.adapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.setupGlobalError(
        throwable: Throwable?,
        errorCode: String
    ) {
        val errorType = getGlobalErrorType(
            throwable = throwable,
            errorCode = errorCode
        )
        errorState.setType(errorType)

        if (errorCode == TokoNowCategoryListViewModel.ERROR_PAGE_NOT_FOUND) {
            setupGlobalErrorPageNotFound()
        } else if (errorCode == TokoNowCategoryListViewModel.ERROR_SERVER || errorCode == TokoNowCategoryListViewModel.ERROR_MAINTENANCE) {
            setupGlobalErrorServerMaintenance()
        } else if (errorType == GlobalError.NO_CONNECTION) {
            setupGlobalErrorNoConnection()
        } else {
            setActionClickTryAgainListener()
        }
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.setupGlobalErrorPageNotFound() {
        errorState.apply {
            errorIllustration.setImage(ERROR_STATE_NOT_FOUND_IMAGE_URL, 0f)
            errorTitle.text = context?.getString(R.string.tokopedianow_category_list_bottom_sheet_error_not_found_title).orEmpty()
            errorDescription.text = context?.getString(R.string.tokopedianow_category_list_bottom_sheet_error_not_found_desc).orEmpty()
        }
        setActionClickTryAgainListener()
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.setupGlobalErrorServerMaintenance() {
        errorState.let {
            it.errorAction.show()
            it.errorAction.text = context?.getString(R.string.tokopedianow_category_list_bottom_sheet_error_go_to_homepage)
            it.setActionClickListener {
                onBackPressed()
            }
        }
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.setupGlobalErrorNoConnection() {
        errorState.setButtonFull(true)
        setActionClickTryAgainListener()
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.setActionClickTryAgainListener() {
        errorState.setActionClickListener {
            getCategoryList()
        }
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.onSuccessGetCategoryListResponse(result: Success<GetCategoryListResponse.CategoryListResponse>) {
        hideLoader()
        if (result.data.header.errorCode.isNullOrEmpty()) {
            adapter?.submitList(result.data.mapToSeeAllCategoryItemUiModel())
        } else {
            showGlobalError(
                throwable = null,
                errorCode = result.data.header.errorCode
            )
        }
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.onFailGetCategoryListResponse(result: Fail) {
        showGlobalError(result.throwable)
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.showGlobalError(throwable: Throwable?, errorCode: String = "") {
        showError()
        setupGlobalError(
            throwable = throwable,
            errorCode = errorCode
        )
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.hideLoader() {
        loadingState.root.hide()
        errorState.hide()
        rvCategoryMenu.show()
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.showLoader() {
        loadingState.root.show()
        rvCategoryMenu.hide()
        errorState.hide()
    }

    private fun FragmentTokopedianowSeeAllCategoryBinding.showError() {
        rvCategoryMenu.hide()
        errorState.show()
    }

    private fun initInjector() {
        DaggerSeeAllCategoryComponent.builder()
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
                when (it) {
                    is Success -> onSuccessGetCategoryListResponse(it)
                    is Fail -> onFailGetCategoryListResponse(it)
                }
            }
        }
    }

    private fun getGlobalErrorType(throwable: Throwable?, errorCode: String = ""): Int {
        return when {
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
        viewModelTokoNow.getCategoryList()
    }

    private fun getWarehouseId(): String {
        return viewModelTokoNow.getWarehouseId()
    }
}
