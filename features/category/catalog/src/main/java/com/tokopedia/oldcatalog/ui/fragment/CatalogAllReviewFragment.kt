package com.tokopedia.oldcatalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.adapter.components.CatalogReviewAdapter
import com.tokopedia.oldcatalog.di.CatalogComponent
import com.tokopedia.oldcatalog.di.DaggerCatalogComponent
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.oldcatalog.model.util.CatalogConstant
import com.tokopedia.oldcatalog.model.util.CatalogUtil
import com.tokopedia.oldcatalog.viewmodel.CatalogAllReviewsViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogAllReviewFragment : BaseViewModelFragment<CatalogAllReviewsViewModel>() {

    @Inject
    lateinit var viewModelFactoryProvider: ViewModelProvider.Factory

    @Inject
    lateinit var catalogAllReviewsViewModel: CatalogAllReviewsViewModel

    private var catalogId: String = ""
    private var catalogName : String = ""

    private var catalogDetailListener: CatalogDetailListener? = null
    private var catalogAdapter : CatalogReviewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_catalog_all_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun initRecyclerView() {
        view?.findViewById<RecyclerView>(R.id.catalog_review_rv)?.apply {
            layoutManager = LinearLayoutManager(view?.context, RecyclerView.VERTICAL, false)
            adapter = catalogAdapter
            show()
        }
    }

    private fun setObservers() {
        catalogAllReviewsViewModel.getCatalogAllReviewsModel().observe(this) {
            catalogAllReviewsViewModel.getCatalogShimmerLiveData().value = false
            when (it) {
                is Success -> {
                    it.data.catalogGetProductReview?.reviewData?.reviews?.let { reviews ->
                        hideGlobalError()
                        catalogAdapter?.submitList(reviews)
                    } ?: kotlin.run {
                        onError(Throwable("No Data"))
                    }
                }

                is Fail -> {
                    onError(it.throwable)
                }
            }
        }

        catalogAllReviewsViewModel.getCatalogShimmerLiveData().observe(this) { isShimmer ->
            if (isShimmer) {
                startShimmer()
            } else {
                stopShimmer()
            }
        }

    }

    private fun afterViewCreated() {
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
        }
        catalogAdapter = CatalogReviewAdapter(arrayListOf(),catalogDetailListener,
            true, catalogName, catalogId)
        initRecyclerView()
        catalogAllReviewsViewModel.getAllReviews(catalogId,CatalogConstant.STAR,CatalogConstant.ZERO_VALUE.toString())
    }

    private fun onError(e: Throwable) {
        showGlobalError()
        view?.findViewById<GlobalError>(R.id.catalog_review_global_error)?.apply {
            if (e is UnknownHostException
                || e is SocketTimeoutException
            ) {
                setType(GlobalError.NO_CONNECTION)
            } else {
                setType(GlobalError.SERVER_ERROR)
            }
            setOnClickListener {
                hideGlobalError()
                catalogAllReviewsViewModel.getAllReviews(catalogId,CatalogConstant.STAR,CatalogConstant.ZERO_VALUE.toString())
            }
        }
    }

    private fun hideGlobalError() {
        view?.findViewById<RecyclerView>(R.id.catalog_review_rv)?.show()
        view?.findViewById<GlobalError>(R.id.catalog_review_global_error)?.hide()
    }

    private fun showGlobalError() {
        view?.findViewById<RecyclerView>(R.id.catalog_review_rv)?.hide()
        view?.findViewById<GlobalError>(R.id.catalog_review_global_error)?.show()
    }

    private fun startShimmer(){
        view?.findViewById<ConstraintLayout>(R.id.catalog_review_shimmer_layout)?.show()
    }

    private fun stopShimmer(){
        view?.findViewById<ConstraintLayout>(R.id.catalog_review_shimmer_layout)?.hide()
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelFactoryProvider
    }

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): CatalogComponent =
            DaggerCatalogComponent.builder()
                    .baseAppComponent((activity?.applicationContext as BaseMainApplication)
                            .baseAppComponent).build()

    override fun getViewModelType(): Class<CatalogAllReviewsViewModel> {
        return CatalogAllReviewsViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        catalogAllReviewsViewModel = viewModel as CatalogAllReviewsViewModel
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val ARG_EXTRA_CATALOG_NAME = "ARG_EXTRA_CATALOG_NAME"

        fun newInstance(catalogName : String, catalogId: String, listener: CatalogDetailListener?): CatalogAllReviewFragment {
            return CatalogAllReviewFragment().apply {
                catalogDetailListener = listener
                val bundle = Bundle()
                bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
                bundle.putString(ARG_EXTRA_CATALOG_NAME, catalogName)
                arguments = bundle
            }
        }
    }
}
