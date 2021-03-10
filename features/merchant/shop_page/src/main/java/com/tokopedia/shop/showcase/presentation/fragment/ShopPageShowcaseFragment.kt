package com.tokopedia.shop.showcase.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.showcase.di.component.DaggerShopPageShowcaseComponent
import com.tokopedia.shop.showcase.di.component.ShopPageShowcaseComponent
import com.tokopedia.shop.showcase.presentation.adapter.ShopPageFeaturedShowcaseAdapter
import com.tokopedia.shop.showcase.presentation.viewmodel.ShopPageShowcaseViewModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Rafli Syam on 05/03/2021
 */
class ShopPageShowcaseFragment : BaseDaggerFragment(), HasComponent<ShopPageShowcaseComponent> {

    companion object {

        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val DEFAULT_SHOP_ID = "0"
        private const val VIEW_LOADING = 1
        private const val VIEW_CONTENT = 2
        private const val VIEW_ERROR = 3

        @JvmStatic
        fun createInstance(
                shopId: String
        ): ShopPageShowcaseFragment = ShopPageShowcaseFragment().apply {
            // set arguments bundle
            arguments = Bundle().apply {
                putString(KEY_SHOP_ID, shopId)
            }
        }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val shopPageShowcaseViewModel: ShopPageShowcaseViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopPageShowcaseViewModel::class.java)
    }

    private var showcaseShimmerView: LinearLayout? = null
    private var featuredShowcaseRv: RecyclerView? = null
    private var featuredShowcaseAdapter: ShopPageFeaturedShowcaseAdapter? = null
    private var tvFeaturedShowcaseTitle: Typography? = null
    private var shopId: String = DEFAULT_SHOP_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_page_showcase, container, false)
        initView(view)
        initRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        loadShowcaseData()
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun getComponent(): ShopPageShowcaseComponent? {
        return activity?.run {
            DaggerShopPageShowcaseComponent
                    .builder()
                    .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun loadShowcaseData() {
        setViewState(VIEW_LOADING)
        shopPageShowcaseViewModel.getShowcaseList(shopId)
    }

    private fun observeLiveData() {

        // showcase list live data
        shopPageShowcaseViewModel.etalaseList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    setViewState(VIEW_CONTENT)
                    featuredShowcaseAdapter?.updateFeaturedShowcaseDataset(it.data)
                }
                is Fail -> { }
            }
        })

    }

    private fun setViewState(viewState: Int) {
        when(viewState) {
            VIEW_LOADING -> {
                showcaseShimmerView?.visible()
                featuredShowcaseRv?.hide()
                tvFeaturedShowcaseTitle?.hide()
            }
            VIEW_CONTENT -> {
                showcaseShimmerView?.hide()
                featuredShowcaseRv?.visible()
                tvFeaturedShowcaseTitle?.visible()
            }
        }
    }

    private fun initView(view: View?) {
        view?.let {
            showcaseShimmerView = it.findViewById(R.id.showcase_loading_shimmer)
            featuredShowcaseRv = it.findViewById(R.id.rvFeaturedShowcase)
            tvFeaturedShowcaseTitle = it.findViewById(R.id.tvFeaturedTitle)

            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        featuredShowcaseAdapter = ShopPageFeaturedShowcaseAdapter()
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        featuredShowcaseRv?.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = featuredShowcaseAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        featuredShowcaseRv?.setMargin(16, 8, 0, 0)
                    } else {
                        featuredShowcaseRv?.setMargin(0, 8, 0, 0)
                    }
                }
            })
        }
    }

    private fun getIntentData() {
        arguments?.let { args ->
            shopId = args.getString(KEY_SHOP_ID, DEFAULT_SHOP_ID)
        }
    }

}