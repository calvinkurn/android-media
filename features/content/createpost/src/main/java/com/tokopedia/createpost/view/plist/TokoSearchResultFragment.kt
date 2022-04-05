package com.tokopedia.createpost.view.plist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.data.non_seller_model.SearchShopModel
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.view.adapter.SearchResultShopListAdapter
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoSearchResultFragment : BaseDaggerFragment() {

    private lateinit var adapter: SearchResultShopListAdapter
    private lateinit var shopResultRecyclerView: RecyclerView
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val shopPageProductListViewModel: ShopPageProductListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(ShopPageProductListViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.content_creation_toko_search_result_fragment, container, false)
        initViews(view)
        return view
    }
    private fun initViews(view: View) {
        shopResultRecyclerView = view.findViewById<RecyclerView>(R.id.content_creator_toko_rv)
        shopResultRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shopPageProductListViewModel.run {
            getShopFirstPageData.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Success -> onSuccessShopData(it.data.aceSearchShop.shopList)
                    else -> {

                    }
                }
            })
        }
    }
    private fun onSuccessShopData(shopList: List<SearchShopModel.AceSearchShop.ShopItem>){
        if (::adapter.isInitialized){
            adapter.updateList(shopList)
            shopResultRecyclerView.adapter = adapter
        }

    }

    override fun getScreenName(): String {
       return SCREEN_NAME
    }

    override fun initInjector() {
        DaggerCreatePostComponent.builder()
            .createPostCommonModule(CreatePostCommonModule(requireContext().applicationContext))
            .createPostModule(CreatePostModule(requireContext().applicationContext)).build()
            .inject(this)
    }
    companion object{
        private val SCREEN_NAME = "ShopSearchPageListResult"
        fun newInstance(bundle: Bundle?): TokoSearchResultFragment {
            val fragment = TokoSearchResultFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}