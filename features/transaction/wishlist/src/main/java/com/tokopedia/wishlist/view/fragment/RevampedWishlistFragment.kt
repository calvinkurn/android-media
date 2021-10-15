package com.tokopedia.wishlist.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.data.model.RevampedWishlistTypeData
import com.tokopedia.wishlist.databinding.FragmentWishlistBinding
import com.tokopedia.wishlist.util.RevampedWishlistConsts
import com.tokopedia.wishlist.view.adapter.RevampedWishlistAdapter

/**
 * Created by fwidjaja on 14/10/21.
 */
class RevampedWishlistFragment : BaseDaggerFragment(), RevampedWishlistAdapter.ActionListener {
    private lateinit var wishlistAdapter: RevampedWishlistAdapter
    private lateinit var scrollRecommendationListener: EndlessRecyclerViewScrollListener
    private var binding by autoClearedNullable<FragmentWishlistBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    companion object {
        @JvmStatic
        fun newInstance(): RevampedWishlistFragment {
            return RevampedWishlistFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
    }

    private fun prepareLayout() {
        wishlistAdapter = RevampedWishlistAdapter().apply {
            setActionListener(this@RevampedWishlistFragment)
        }
        addEndlessScrollListener()
        renderWishlist()
    }

    private fun addEndlessScrollListener() {
        val glm = GridLayoutManager(activity, 2)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (wishlistAdapter.getItemViewType(position)) {
                    RevampedWishlistAdapter.LAYOUT_LIST -> 2
                    RevampedWishlistAdapter.LAYOUT_GRID -> 1
                    else -> 2
                }
            }
        }

        scrollRecommendationListener = object : EndlessRecyclerViewScrollListener(glm) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                currentPage += 1
            }
        }

        binding?.run {
            rvWishlist.apply {
                layoutManager = glm
                adapter = wishlistAdapter
                addOnScrollListener(scrollRecommendationListener)
            }
        }
    }

    private fun renderWishlist() {
        val productModel = ProductCardModel(
                productImageUrl = "https://i.ibb.co/QHHXdm0/sample-product-img.png",
                isWishlistVisible = true,
                productName = "2 Lines Product Name on List View on SRP with Smaller Image on the...",
                shopName = "Testing Shop",
                formattedPrice = "Rp.7.999.000",
                slashedPrice = "Rp.8.999.000",
                shopLocation = "Jakarta Pusat",
                shopRating = "4.5",
                isShopRatingYellow = true,
                hasSecondaryButton = true,
                hasTambahKeranjangButton = true
        )
        val listItem = arrayListOf<RevampedWishlistTypeData>()
        listItem.add(RevampedWishlistTypeData(productModel, RevampedWishlistConsts.TYPE_LIST))
        listItem.add(RevampedWishlistTypeData(productModel, RevampedWishlistConsts.TYPE_LIST))
        listItem.add(RevampedWishlistTypeData(productModel, RevampedWishlistConsts.TYPE_LIST))
        wishlistAdapter.addList(listItem)
    }
}