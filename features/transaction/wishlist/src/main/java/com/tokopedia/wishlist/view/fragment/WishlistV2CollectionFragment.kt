package com.tokopedia.wishlist.view.fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.databinding.FragmentCollectionWishlistV2Binding
import com.tokopedia.wishlist.di.DaggerWishlistV2CollectionComponent
import com.tokopedia.wishlist.di.WishlistV2CollectionModule


class WishlistV2CollectionFragment : BaseDaggerFragment() {
    private var binding by autoClearedNullable<FragmentCollectionWishlistV2Binding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let { activity ->
            DaggerWishlistV2CollectionComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .wishlistV2CollectionModule(WishlistV2CollectionModule(activity))
                .build()
                .inject(this)
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    companion object {
        @JvmStatic
        fun newInstance(): WishlistV2CollectionFragment {
            return WishlistV2CollectionFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionWishlistV2Binding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        addImageList()
    }

    private fun prepareLayout() {
        binding?.run {
            wishlistCollectionNavtoolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE)
            wishlistCollectionNavtoolbar.setToolbarTitle("Wishlist")
        }
    }

    private fun addImageList() {
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
    }

    private fun toDp(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}