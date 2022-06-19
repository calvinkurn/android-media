package com.tokopedia.wishlist.view.fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.CollectionWishlistTypeLayoutData
import com.tokopedia.wishlist.data.model.response.CollectionWishlistResponse
import com.tokopedia.wishlist.databinding.FragmentCollectionWishlistBinding
import com.tokopedia.wishlist.di.CollectionWishlistModule
import com.tokopedia.wishlist.di.DaggerCollectionWishlistComponent
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COLLECTION_CREATE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COLLECTION_TICKER
import com.tokopedia.wishlist.view.adapter.CollectionWishlistAdapter
import com.tokopedia.wishlist.view.viewmodel.CollectionWishlistViewModel
import javax.inject.Inject


class CollectionWishlistFragment : BaseDaggerFragment(), CollectionWishlistAdapter.ActionListener {
    private var binding by autoClearedNullable<FragmentCollectionWishlistBinding>()
    private lateinit var collectionAdapter: CollectionWishlistAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val collectionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CollectionWishlistViewModel::class.java]
    }
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let { activity ->
            DaggerCollectionWishlistComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .collectionWishlistModule(CollectionWishlistModule(activity))
                .build()
                .inject(this)
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    companion object {
        @JvmStatic
        fun newInstance(): CollectionWishlistFragment {
            return CollectionWishlistFragment()
        }

        const val DEFAULT_TITLE = "Wishlist"
        private const val OK = "OK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            getWishlistCollections()
        } else {
            startActivityForResult(
                RouteManager.getIntent(context, ApplinkConst.LOGIN),
                WishlistV2Fragment.REQUEST_CODE_LOGIN
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionWishlistBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingData()
    }

    private fun observingData() {
        observingWishlistCollections()
    }

    private fun prepareLayout() {
        setToolbarTitle(DEFAULT_TITLE)
        setSwipeRefreshLayout()
        collectionAdapter = CollectionWishlistAdapter().apply {
            setActionListener(this@CollectionWishlistFragment)
        }
        binding?.run {
            rvWishlistCollection.apply {
                layoutManager = GridLayoutManager(context, 2).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when (collectionAdapter.getItemViewType(position)) {
                                CollectionWishlistAdapter.LAYOUT_COLLECTION_TICKER -> 2
                                else -> 1
                            }
                        }
                    }
                }
                adapter = collectionAdapter
            }
        }
    }

    private fun setToolbarTitle(title: String) {
        binding?.run {
            wishlistCollectionNavtoolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE)
            wishlistCollectionNavtoolbar.setToolbarTitle(title)
        }
    }

    private fun setSwipeRefreshLayout() {
        binding?.run {
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.setOnRefreshListener {
                doRefresh()
            }
        }
    }

    private fun doRefresh() {
        getWishlistCollections()
        collectionAdapter.resetTicker()
    }

    private fun getWishlistCollections() {
        collectionViewModel.getWishlistCollections()
    }

    private fun observingWishlistCollections() {
        collectionViewModel.collections.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    if (result.data.status == OK) {
                        collectionAdapter.addList(mapCollection(result.data.data))
                    } else {
                        // TODO: show global error page?
                        val errorMessage = result.data.errorMessage.first().ifEmpty { context?.getString(
                            R.string.wishlist_v2_common_error_msg) }
                        errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    // TODO: show global error page?
                    finishRefresh()
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun mapCollection(data: CollectionWishlistResponse.Data.GetWishlistCollections.WishlistCollectionResponseData): List<CollectionWishlistTypeLayoutData> {
        val listCollection = arrayListOf<CollectionWishlistTypeLayoutData>()
        val tickerObject = CollectionWishlistTypeLayoutData(data.ticker, TYPE_COLLECTION_TICKER)
        listCollection.add(tickerObject)

        data.collections.forEach { item ->
            val collectionItemObject = CollectionWishlistTypeLayoutData(item, TYPE_COLLECTION_ITEM)
            listCollection.add(collectionItemObject)
        }

        val createNewItem = CollectionWishlistTypeLayoutData(data.placeholder, TYPE_COLLECTION_CREATE)
        listCollection.add(createNewItem)

        return listCollection
    }

    private fun showToaster(message: String, actionText: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_LONG, type, actionText).show()
        }
    }

    private fun finishRefresh() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    /*private fun addImageList() {
        binding?.run {
            val params: GridLayout.LayoutParams = GridLayout.LayoutParams(testCardImage.imgCollection1.layoutParams)
            params.rowSpec = GridLayout.spec(0, 2) // First cell in first row use rowSpan 2.
            params.height = toDp(154)
            testCardImage.imgCollection1.layoutParams = params
            testCardImage.imgCollection1.setImageUrl("https://ecs7.tokopedia.net/img/cache/300/VqbcmM/2022/3/1/810d40b6-385b-4f33-ae37-03b92c552f9a.jpg")
            testCardImage.imgCollection1.scaleType = ImageView.ScaleType.CENTER_CROP

            testCardImage.imgCollection2.setImageUrl("https://ecs7.tokopedia.net/img/cache/300/VqbcmM/2022/3/9/a18a95e6-87bb-4a38-b596-fd81312e8d02.jpg")
            testCardImage.imgCollection3.setImageUrl("https://ecs7.tokopedia.net/img/cache/300/VqbcmM/2020/12/29/0d60ce27-26f1-4f1e-b2aa-8d3d5f73d7c4.jpg")
            testCardImage.imgCollection4.gone()

            testCardNewCollection.wishlistCollectionCreateNew.setImageUrl("https://images.tokopedia.net/img/android/wishlist_collection/bg_create_new.png")

            testTickerCollection.apply {
                collectionTickerCard.cardType = CardUnify2.TYPE_SHADOW
                icCloseTickerCollectionWishlist.setOnClickListener { println("++ keclick close") }
                icCloseTickerCollectionWishlist.bringToFront()
                wishlistCollectionTickerTitle.text = "Pakai fitur Koleksi, Wishlist jadi rapi"
                wishlistCollectionTickerDesc.text = "Kelompokkan barang di Wishlist sesukamu"
            }
        }
    }*/

    override fun onCloseTicker() {
        collectionAdapter.hideTicker()
    }

    override fun onCreateNewCollection() {
        println("++ onCreateNewCollection")
    }
}