package com.tokopedia.inbox.universalinbox.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.inbox.databinding.UniversalInboxFragmentBinding
import com.tokopedia.inbox.universalinbox.di.UniversalInboxComponent
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxAdapter
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxEndlessScrollListener
import com.tokopedia.inbox.universalinbox.view.viewmodel.UniversalInboxViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class UniversalInboxFragment:
    BaseDaggerFragment(),
    UniversalInboxEndlessScrollListener.Listener
{

    private var binding: UniversalInboxFragmentBinding? by autoClearedNullable()
    private var adapter = UniversalInboxAdapter()
    private var endlessRecyclerViewScrollListener: UniversalInboxEndlessScrollListener? = null

    @Inject
    lateinit var viewModel: UniversalInboxViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UniversalInboxFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        return binding?.root
    }

    override fun initInjector() {
        getComponent(UniversalInboxComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view, savedInstanceState)
    }

    private fun initViews(view: View, savedInstanceState: Bundle?) {
        setupChatRoomRecyclerView()
        setupRecyclerViewLoadMore()
        adapter.addItems(viewModel.dummy())
    }

    private fun setupChatRoomRecyclerView() {
        binding?.inboxRv?.layoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL)
        binding?.inboxRv?.setHasFixedSize(true)
        binding?.inboxRv?.itemAnimator = null
        binding?.inboxRv?.adapter = adapter
    }

    private fun setupRecyclerViewLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            binding?.inboxRv?.layoutManager?.let {
                endlessRecyclerViewScrollListener = UniversalInboxEndlessScrollListener(
                    layoutManager = it as StaggeredGridLayoutManager,
                    this@UniversalInboxFragment
                )
            }
        }
        endlessRecyclerViewScrollListener?.let {
            binding?.inboxRv?.addOnScrollListener(it)
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        //TODO
        viewModel.loadMoreRecommendation()
    }

    companion object {
        private const val TAG = "UniversalInboxFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): UniversalInboxFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UniversalInboxFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UniversalInboxFragment::class.java.name
            ).apply {
                arguments = bundle
            } as UniversalInboxFragment
        }
    }
}
