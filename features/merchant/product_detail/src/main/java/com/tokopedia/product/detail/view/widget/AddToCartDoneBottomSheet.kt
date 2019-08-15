package com.tokopedia.product.detail.view.widget

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductViewModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationViewModel
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.AddToCartDoneAdapter
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.product.detail.view.viewmodel.Loaded
import com.tokopedia.product.detail.view.viewmodel.Loading
import com.tokopedia.product.detail.view.viewmodel.ProductInfoViewModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddToCartDoneBottomSheet : BottomSheets(), AddToCartDoneAddedProductViewHolder.AddToCartDoneAddedProductListener,
        RecommendationProductAdapter.UserActiveListener, HasComponent<ProductDetailComponent> {
    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent(
                    (activity?.applicationContext as BaseMainApplication).baseAppComponent
            ).build()

    override val isUserSessionActive: Boolean
        get() = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productInfoViewModel: ProductInfoViewModel
    lateinit var atcDoneAdapter: AddToCartDoneAdapter
    override fun onButtonGoToCartClicked() {

    }

    lateinit var rv: RecyclerView
    var model: AddToCartDoneAddedProductViewModel? = null
    override fun getLayoutResourceId(): Int {
        return R.layout.add_to_cart_done_bottomsheet
    }

    override fun initView(view: View?) {
        view?.let {
            rv = it.findViewById(R.id.recycler_view_add_to_cart_top_ads)
        }
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        initInjector()
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        productInfoViewModel = viewModelProvider.get(ProductInfoViewModel::class.java)
        productInfoViewModel.loadTopAdsProduct.observe(this, Observer {
            when (it) {
                is Loading -> {
//                    loadingRecommendationView()
                }
                is Loaded -> {
                    (it.data as? Success)?.data?.let { result ->
                        atcDoneAdapter.clearAllElements()
                        renderRecommendationData(result)
                        atcDoneAdapter.addElement(0,model)
                        atcDoneAdapter.notifyDataSetChanged()
                    }
                }
            }
        })

        arguments?.let {
            model = it.getParcelable("model")
        }
        val trackingQueue = TrackingQueue(context!!)
        val factory = AddToCartDoneTypeFactory(
                this,
                this,
                trackingQueue
        )
        atcDoneAdapter = AddToCartDoneAdapter(factory)
        val linearLayoutManager = LinearLayoutManager(context)
        rv.layoutManager = linearLayoutManager
        rv.adapter = atcDoneAdapter
        val ggz = LoadingMoreModel()
        atcDoneAdapter.addElement(model)
        atcDoneAdapter.addElement(ggz)
        model?.productId?.let { productInfoViewModel.loadTopAds(it) }


//        val md2 = AddToCartDoneRecommendationViewModel(
//                "pr",
//                "rqwe"
//        )
//        atcDoneAdapter.addElement(md2)
//        val md3 = AddToCartDoneRecommendationViewModel(
//                "pr",
//                "rqwe"
//        )
//        atcDoneAdapter.addElement(md3)
        atcDoneAdapter.notifyDataSetChanged()
    }

    private fun renderRecommendationData(result: List<RecommendationWidget>) {
        for (res in result) {
            atcDoneAdapter.addElement(AddToCartDoneRecommendationViewModel(res))
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun gotoCart() {
//        activity?.let {
//            if (productInfoViewModel.isUserSessionActive()) {
//                startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
//            } else {
//                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
//                        REQUEST_CODE_LOGIN)
//            }
//            productDetailTracking.eventCartMenuClicked(generateVariantString())
//        }
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }
}