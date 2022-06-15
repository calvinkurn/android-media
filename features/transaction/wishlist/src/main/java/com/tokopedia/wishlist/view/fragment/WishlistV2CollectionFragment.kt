package com.tokopedia.wishlist.view.fragment

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
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
}