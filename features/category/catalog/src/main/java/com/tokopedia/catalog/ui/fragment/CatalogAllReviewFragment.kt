package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.components.CatalogReviewAdapter
import com.tokopedia.catalog.di.CatalogComponent
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.catalog.viewmodel.CatalogAllReviewsViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogAllReviewFragment : BaseViewModelFragment<CatalogAllReviewsViewModel>() {

    @Inject
    lateinit var viewModelFactoryProvider: ViewModelProvider.Factory

    @Inject
    lateinit var catalogAllReviewsViewModel: CatalogAllReviewsViewModel

    private var catalogId: String = ""

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
        view?.findViewById<RecyclerView>(R.id.review_rv_catalog)?.apply {
            layoutManager = LinearLayoutManager(view?.context, RecyclerView.VERTICAL, false)
            adapter = catalogAdapter
            show()
        }
    }

    private fun setObservers() {
        catalogAllReviewsViewModel.getCatalogAllReviewsModel().observe(this,{
            when (it) {
                is Success -> {
                    renderData(it.data.catalogGetProductReview?.reviewData)
                    it.data.catalogGetProductReview?.reviewData?.reviews?.let { reviews ->
                        catalogAdapter?.submitList(reviews)
                    } ?: kotlin.run {

                    }
                }

                is Fail -> {

                }
            }
        })
    }

    private fun renderData(reviewData: CatalogProductReviewResponse.CatalogGetProductReview.ReviewData?) {
        view?.findViewById<Typography>(R.id.review_rating_catalog)?.displayTextOrHide(reviewData?.avgRating ?: "")
        if(reviewData?.avgRating?.isBlank() == true){
            view?.findViewById<IconUnify>(R.id.rating_review_star_catalog)?.hide()
        }else {
            view?.findViewById<IconUnify>(R.id.rating_review_star_catalog)?.show()
        }
        view?.findViewById<Typography>(R.id.review_count_catalog)?.displayTextOrHide("dari ${reviewData?.totalHelpfulReview } ulasan membantu")

    }

    private fun afterViewCreated() {
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
        }
        catalogAdapter = CatalogReviewAdapter(arrayListOf(),catalogDetailListener)
        initRecyclerView()
        catalogAllReviewsViewModel.getAllReviews(catalogId,"star","5")
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

        fun newInstance(catalogId: String, listener: CatalogDetailListener?): CatalogAllReviewFragment {
            return CatalogAllReviewFragment().apply {
                catalogDetailListener = listener
                val bundle = Bundle()
                bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
                arguments = bundle
            }
        }
    }
}