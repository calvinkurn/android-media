package com.tokopedia.people.views.fragment.bottomsheet

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.people.databinding.UpBottomSheetOptionBinding
import com.tokopedia.people.R
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.views.adapter.Option
import com.tokopedia.people.views.adapter.UserProfileOptionAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by kenny.hadisaputra on 28/11/22
 */
class UserProfileOptionBottomSheet : BottomSheetUnify() {

    private var _binding: UpBottomSheetOptionBinding? = null
    private val binding: UpBottomSheetOptionBinding
        get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        UserProfileOptionAdapter()
    }

    private val dp18 by lazy(LazyThreadSafetyMode.NONE) {
        18.dpToPx(binding.root.resources.displayMetrics)
    }

    private var mListener: Listener? = null
    private var mDataSource: DataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
        mDataSource = null
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    private fun setupBottomSheet() {
        _binding = UpBottomSheetOptionBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.adapter = adapter
        binding.root.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = dp18
                outRect.bottom = dp18
            }
        })

        adapter.submitList(getOptions())
    }

    private fun getOptions(): List<Option> {
        val isBlocking = mDataSource?.isBlocking() == true
        return listOf(
            Option(
                getString(R.string.up_report_user),
                MethodChecker.getColor(
                    requireContext(),
                    unifyR.color.Unify_RN500,
                )
            ) { mListener?.onReportUser(this) },
            Option(
                getString(
                    if (isBlocking) R.string.up_unblock_user
                    else R.string.up_block_user
                ),
                MethodChecker.getColor(
                    requireContext(),
                    unifyR.color.Unify_NN950,
                ),
            ) { mListener?.onBlockingUser(this, shouldBlock = !isBlocking) },
        )
    }

    companion object {
        private const val TAG = "UserProfileOptionBottomSheet"

        fun get(
            fragmentManager: FragmentManager,
        ): UserProfileOptionBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? UserProfileOptionBottomSheet
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): UserProfileOptionBottomSheet {
            return get(fragmentManager) ?: run {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    UserProfileOptionBottomSheet::class.java.name
                ) as UserProfileOptionBottomSheet
            }
        }
    }

    interface Listener {
        fun onBlockingUser(bottomSheet: UserProfileOptionBottomSheet, shouldBlock: Boolean)
        fun onReportUser(bottomSheet: UserProfileOptionBottomSheet)
    }

    interface DataSource {
        fun isBlocking(): Boolean
    }
}
