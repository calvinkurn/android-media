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
@Deprecated(
    replaceWith = ReplaceWith(
        "",
        "com.tokopedia.creation.common.presentation.bottomsheet.ContentCreationBottomSheet"
    ),
    message = ""
)
class ShopPageHeaderContentCreationOptionBottomSheet : BottomSheetUnify() {

    private var mListener: Listener? = null
    private var mBroadcasterConfig = Broadcaster.Config()

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
        isShowShortsEntryPoint(mBroadcasterConfig.shortVideoAllowed)
        isShowBroadcasterEntryPoint(mBroadcasterConfig.streamAllowed)
        isShowPerformanceDashboardEntryPoint(mBroadcasterConfig.hasContent)
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
        if (!isAdded) show(fragmentManager, TAG)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setData(broadcasterConfig: Broadcaster.Config) {
        mBroadcasterConfig = broadcasterConfig
    }

    private fun isShowShortsEntryPoint(show: Boolean) {
        binding.llShorts.showWithCondition(show)
        binding.divider1.showWithCondition(show)
    }

    private fun isShowBroadcasterEntryPoint(show: Boolean) {
        binding.llBroadcaster.showWithCondition(show)
        binding.divider2.showWithCondition(show)
    }

    private fun isShowPerformanceDashboardEntryPoint(show: Boolean) {
        binding.llPerformanceDashboard.showWithCondition(show)
    }

    companion object {
        private const val TAG = "ShopContentCreationOptionBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ShopPageHeaderContentCreationOptionBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? ShopPageHeaderContentCreationOptionBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ShopPageHeaderContentCreationOptionBottomSheet::class.java.name
            ) as ShopPageHeaderContentCreationOptionBottomSheet
        }
    }

    interface Listener {
        fun onShortsCreationClicked()
        fun onBroadcastCreationClicked()
        fun onPerformanceDashboardEntryClicked()
    }
}
