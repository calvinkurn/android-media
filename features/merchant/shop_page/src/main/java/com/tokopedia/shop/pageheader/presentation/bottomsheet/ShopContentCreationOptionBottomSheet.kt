package com.tokopedia.shop.pageheader.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.databinding.BottomSheetShopContentCreationOptionBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on December 01, 2022
 */
class ShopContentCreationOptionBottomSheet : BottomSheetUnify() {

    private var mListener: Listener? = null
    private var mBroadcasterConfig: Broadcaster.Config = Broadcaster.Config(
        streamAllowed = false,
        shortVideoAllowed = false,
    )

    private var _binding: BottomSheetShopContentCreationOptionBinding? = null
    private val binding: BottomSheetShopContentCreationOptionBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun setupBottomSheet(inflater: LayoutInflater) {
        _binding = BottomSheetShopContentCreationOptionBinding.inflate(inflater)
        setChild(binding.root)

        clearContentPadding = true
    }

    private fun setupView() {
        shortsEntryPoint(mBroadcasterConfig.shortVideoAllowed)
        broadcasterEntryPoint(mBroadcasterConfig.streamAllowed)
    }

    private fun setupListener() {
        binding.llShorts.setOnClickListener {
            mListener?.onShortsCreationClicked()
            dismiss()
        }

        binding.llBroadcaster.setOnClickListener {
            mListener?.onBroadcastCreationClicked()
            dismiss()
        }

        binding.llPerformanceDashboard.setOnClickListener {
            mListener?.onPerformanceDashboardEntryClicked()
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setData(broadcasterConfig: Broadcaster.Config) {
        mBroadcasterConfig = broadcasterConfig
    }

    private fun shortsEntryPoint(show: Boolean) {
        binding.llShorts.showWithCondition(show)
        binding.divider1.showWithCondition(show)
    }

    private fun broadcasterEntryPoint(show: Boolean) {
        binding.llBroadcaster.showWithCondition(show)
        binding.divider2.showWithCondition(show)
    }

    companion object {
        private const val TAG = "ShopContentCreationOptionBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): ShopContentCreationOptionBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ShopContentCreationOptionBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ShopContentCreationOptionBottomSheet::class.java.name
            ) as ShopContentCreationOptionBottomSheet
        }
    }

    interface Listener {
        fun onShortsCreationClicked()
        fun onBroadcastCreationClicked()
        fun onPerformanceDashboardEntryClicked()
    }

}
