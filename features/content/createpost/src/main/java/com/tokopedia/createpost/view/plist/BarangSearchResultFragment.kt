package com.tokopedia.createpost.view.plist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.view.plist.ErrorMessage
import com.tokopedia.createpost.common.view.plist.Loading
import com.tokopedia.createpost.common.view.plist.ShopPageListener
import com.tokopedia.createpost.common.view.plist.ShopPageProduct
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.library.baseadapter.AdapterCallback
import javax.inject.Inject

class BarangSearchResultFragment: BaseDaggerFragment(),AdapterCallback, ShopPageListener {

    private lateinit var productResultRecyclerView: RecyclerView
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val shopPageProductListViewModel: ShopPageProductListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(ShopPageProductListViewModel::class.java)
    }
    private val searchResultShopListAdapter: ShopProductListBaseAdapter by lazy {
        ShopProductListBaseAdapter(
            shopPageProductListViewModel,
            this,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.content_creation_barang_search_result_fragment, container, false)
        initViews(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shopPageProductListViewModel.run {
            shopPageProductListViewModel.productList.observe(viewLifecycleOwner, Observer {

                    it?.let {
                        when (it) {
                            is Loading -> {
                                searchResultShopListAdapter.resetAdapter()
                                searchResultShopListAdapter.notifyDataSetChanged()
                            }
                            is com.tokopedia.createpost.common.view.plist.Success -> {
                                searchResultShopListAdapter.onSuccess(it.data)
                            }
                            is ErrorMessage -> {
                                searchResultShopListAdapter.onError()
                            }
                        }
                    }
                })

        }
    }

    private fun initViews(view: View) {
        productResultRecyclerView = view.findViewById<RecyclerView>(R.id.content_creator_barang_rv)
        productResultRecyclerView.run {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = searchResultShopListAdapter

        }
        searchResultShopListAdapter.resetAdapter()

    }

    override fun getScreenName(): String {
       return SCREEN_NAME
    }
    fun onSuccessProduct() {

    }


    override fun initInjector() {
        DaggerCreatePostComponent.builder()
            .createPostCommonModule(CreatePostCommonModule(requireContext().applicationContext))
            .createPostModule(CreatePostModule(requireContext().applicationContext)).build()
            .inject(this)
    }
    companion object{
        private val SCREEN_NAME = "ProductSearchPageListResult"
        fun newInstance(bundle: Bundle?): BarangSearchResultFragment {
            val fragment = BarangSearchResultFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun shopProductImpressed(position: Int, product: ShopPageProduct) {
        TODO("Not yet implemented")
    }

    override fun shopProductClicked(position: Int, product: ShopPageProduct) {
        TODO("Not yet implemented")
    }

    override fun sortProductCriteriaClicked(criteria: String) {
        TODO("Not yet implemented")
    }

    override fun onRetryPageLoad(pageNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun onEmptyList(rawObject: Any?) {
        TODO("Not yet implemented")
    }

    override fun onStartFirstPageLoad() {
        TODO("Not yet implemented")
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        TODO("Not yet implemented")
    }

    override fun onStartPageLoad(pageNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
        TODO("Not yet implemented")
    }

    override fun onError(pageNumber: Int) {
        TODO("Not yet implemented")
    }
}