package com.tokopedia.content.common.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.databinding.BottomSheetAccountTypeBinding
import com.tokopedia.content.common.ui.adapter.FeedAccountTypeAdapter
import com.tokopedia.content.common.ui.itemdecoration.FeedAccountTypeItemDecoration
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.viewholder.FeedAccountTypeViewHolder
import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalytic
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.content.common.R

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
class ContentAccountTypeBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetAccountTypeBinding? = null
    private val binding: BottomSheetAccountTypeBinding
        get() = _binding!!

    private val adapter: FeedAccountTypeAdapter by lazy {
        FeedAccountTypeAdapter(object : FeedAccountTypeViewHolder.Listener {
            override fun onClick(item: ContentAccountUiModel) {
                mAnalytic?.clickAccountTypeItem(item)
                dismiss()
                mListener?.onAccountClick(item)
            }
        })
    }

    private val mFeedAccountList = mutableListOf<ContentAccountUiModel>()
    private var mListener: Listener? = null
    private var mAnalytic: FeedAccountTypeAnalytic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mListener = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetAccountTypeBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)

        setCloseClickListener {
            mListener?.onClickClose()
            dismiss()
        }
    }

    private fun setupView() {
        setTitle(getString(R.string.feed_account_bottom_sheet_title))

        binding.rvFeedAccount.addItemDecoration(FeedAccountTypeItemDecoration(requireContext()))
        binding.rvFeedAccount.adapter = adapter

        adapter.updateData(mFeedAccountList)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    fun setData(contentAccountList: List<ContentAccountUiModel>): ContentAccountTypeBottomSheet {
        mFeedAccountList.clear()
        mFeedAccountList.addAll(contentAccountList)

        if(isAdded) adapter.updateData(mFeedAccountList)

        return this
    }

    fun setAnalytic(analytic: FeedAccountTypeAnalytic) {
        mAnalytic = analytic
    }

    companion object {
        private const val TAG = "FeedAccountTypeBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): ContentAccountTypeBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ContentAccountTypeBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ContentAccountTypeBottomSheet::class.java.name
            ) as ContentAccountTypeBottomSheet
        }
    }

    interface Listener {
        fun onAccountClick(contentAccount: ContentAccountUiModel)
        fun onClickClose()
    }
}
