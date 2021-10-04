package com.tokopedia.seller_migration_common.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.databinding.FragmentBaseSellerFeatureBinding
import com.tokopedia.seller_migration_common.presentation.StaticDataProvider
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.utils.view.binding.noreflection.viewBinding

abstract class BaseSellerFeatureTabFragment(
        private val staticDataProvider: StaticDataProvider
) : Fragment() {

    var recyclerViewListener: SellerFeatureCarousel.RecyclerViewListener? = null

    private val binding by viewBinding(FragmentBaseSellerFeatureBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_seller_feature, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.sellerFeatureCarousel?.run {
            toggleDivider(false)
            toggleTitle(false)
            recyclerViewListener?.let { setRecyclerViewListener(it) }
            setRecyclerViewLayoutManager(GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false))
            setItems(staticDataProvider.getData())
        }
    }
}