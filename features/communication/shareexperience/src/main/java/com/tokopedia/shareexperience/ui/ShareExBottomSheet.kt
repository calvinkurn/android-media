package com.tokopedia.shareexperience.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.shareexperience.data.di.DaggerShareExComponent
import com.tokopedia.shareexperience.databinding.ShareexperienceBottomSheetBinding
import com.tokopedia.shareexperience.ui.adapter.ShareExBottomSheetAdapter
import com.tokopedia.shareexperience.ui.adapter.decoration.ShareExBottomSheetSpacingItemDecoration
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactoryImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShareExBottomSheet: BottomSheetUnify() {

    @Inject
    lateinit var viewModel: ShareExViewModel

    private var viewBinding by autoClearedNullable<ShareexperienceBottomSheetBinding>()
    private val adapter by lazy {
        ShareExBottomSheetAdapter(
            ShareExTypeFactoryImpl()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater) {
        viewBinding = ShareexperienceBottomSheetBinding.inflate(inflater)
        setChild(viewBinding?.root)
        clearContentPadding = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    private fun initInjector() {
        DaggerShareExComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        initializeRecyclerView()
        initObservers()
        viewModel.processAction(ShareExBottomSheetAction.InitializePage)
    }

    private fun setTitle() {
        bottomSheetTitle.text = "Bagikan ke teman kamu" //TODO: change to data from BE
    }

    private fun initializeRecyclerView() {
        context?.let {
            viewBinding?.shareexRvBottomSheet?.adapter = adapter
            viewBinding?.shareexRvBottomSheet?.itemAnimator = null
            viewBinding?.shareexRvBottomSheet?.isNestedScrollingEnabled = false
            viewBinding?.shareexRvBottomSheet?.layoutManager = LinearLayoutManager(it)
            viewBinding?.shareexRvBottomSheet?.addItemDecoration(
                ShareExBottomSheetSpacingItemDecoration(
                    8.dpToPx(it.resources.displayMetrics),
                    16.dpToPx(it.resources.displayMetrics)
                )
            )
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeBottomSheetUiState()
                }
            }
        }
        viewModel.setupViewModelObserver()
    }

    private suspend fun observeBottomSheetUiState() {
        viewModel.bottomSheetUiState.collectLatest {
            it.uiModelList?.let { newList ->
                setBottomSheetData(newList = newList)
            }
        }
    }

    private fun setBottomSheetData(newList: List<Visitable<in ShareExTypeFactory>>) {
        adapter.updateItems(newList)
    }
}
