package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.DaggerThankYouPageComponent
import com.tokopedia.thankyou_native.presentation.adapter.PDPThankYouPageFactory
import com.tokopedia.thankyou_native.presentation.adapter.PdpThankYouPageAdapter
import com.tokopedia.thankyou_native.presentation.adapter.model.PDPItemAdapterModel
import com.tokopedia.thankyou_native.helper.ProductCardDefaultDecorator
import com.tokopedia.thankyou_native.presentation.viewModel.PDPThankYouViewModel
import com.tokopedia.thankyou_native.presentation.viewModel.state.Loaded
import com.tokopedia.thankyou_native.presentation.viewModel.state.Loading
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_pdp_recommendation.view.*
import javax.inject.Inject

class PDPThankYouPageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var viewModel: PDPThankYouViewModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dataList: ArrayList<Visitable<*>>

    private lateinit var adapter: PdpThankYouPageAdapter

    var listener: RecommendationListener? = null

    fun getLayout() = R.layout.thank_pdp_recommendation

    init {
        injectComponents()
        initUI()
    }

    private fun injectComponents() {
        DaggerThankYouPageComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
            viewModel = viewModelProvider[PDPThankYouViewModel::class.java]
        }
    }

    private fun initUI() {
        val v = LayoutInflater.from(context).inflate(getLayout(), this, true)
        setupRecyclerView(v)
        viewModel?.let {
            startViewModelObserver(it)
            loadRecommendation(it)
        }
    }

    private fun loadRecommendation(pdpThankYouViewModel: PDPThankYouViewModel) {
        pdpThankYouViewModel.loadRecommendationData()
    }

    private fun startViewModelObserver(pdpThankYouViewModel: PDPThankYouViewModel) {
        pdpThankYouViewModel.titleLiveData.observe(context as AppCompatActivity, Observer {
            when (it) {
                is Loaded -> {
                    (it.data as? Success)?.data?.let { title ->
                        tvTitle.text = title
                    }
                }
            }
        })

        pdpThankYouViewModel.recommendationListModel.observe(context as AppCompatActivity, Observer {
            when (it) {
                is Loading -> {
                }
                is Loaded -> {
                    (it.data as? Success)?.data?.let { result ->
                        addItemsToAdapter(result)
                    }
                }
            }
        })
    }

    private fun addItemsToAdapter(result: List<RecommendationWidget>) {
        result.forEach { it ->
            val list = it.recommendationItemList.map { recommendationItem ->
                PDPItemAdapterModel(recommendationItem)
            }
            dataList.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView(view: View) {
        dataList = ArrayList()
        listener = getRecommendationListener()
        val typeFactory = PDPThankYouPageFactory(listener!!)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        adapter = PdpThankYouPageAdapter(dataList, typeFactory)
        recyclerView.adapter =  adapter
        recyclerView.addItemDecoration(ProductCardDefaultDecorator())
    }

    private fun getRecommendationListener(): RecommendationListener {
        return object : RecommendationListener {
            override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
            }

            override fun onProductImpression(item: RecommendationItem) {
            }

            override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
            }

        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }


}