package com.tokopedia.privacycenter.ui.searchhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ItemSearch
import com.tokopedia.privacycenter.databinding.FragmentSearchHistoryBinding
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.domain.DeleteSearchHistoryResult
import com.tokopedia.privacycenter.utils.getMessage
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SearchHistoryFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentSearchHistoryBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            SearchHistoryViewModel::class.java
        )
    }

    private var adapter: SearchHistoryAdapter? = null
    private var loaderDeleteAllSearchHistory: LoaderDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initListener()
    }

    private fun initListener() {
        binding?.localLoad?.setOnClickListener {
            viewModel.getSearchHistory()
        }

        binding?.textDeleteAll?.setOnClickListener {
            showDialogDeleteAll()
        }
    }

    private fun initObserver() {
        viewModel.listSearchHistory.observe(viewLifecycleOwner) {
            when (it) {
                is PrivacyCenterStateResult.Loading -> {
                    onLoading()
                }
                is PrivacyCenterStateResult.Success -> {
                    setUpList(it.data)
                }
                is PrivacyCenterStateResult.Fail -> {
                    onFailed(it.error)
                }
            }
        }

        viewModel.deleteSearchHistory.observe(viewLifecycleOwner) {
            when (it) {
                is DeleteSearchHistoryResult.Success -> {
                    successDeleteHistory(it.position, it.isClearAll)
                }
                is DeleteSearchHistoryResult.Failed -> {
                    failedDeleteHistory(it.position, it.isClearAll, it.throwable)
                }
            }
        }
    }

    private fun successDeleteHistory(position: Int, clearAll: Boolean) {
        if (clearAll) {
            loaderDeleteAllSearchHistory?.dismiss()
            onEmpty()
        } else {
            adapter?.removeItem(position)
            if (adapter?.itemCount == 0) {
                onEmpty()
            }
        }
    }

    private fun failedDeleteHistory(position: Int, clearAll: Boolean, throwable: Throwable?) {
        if (clearAll) {
            loaderDeleteAllSearchHistory?.dismiss()
            showListSearchHistory()
        } else {
            adapter?.notifyItemChanged(position)
        }

        val message = throwable?.getMessage(requireActivity())
            ?: if (clearAll) {
                requireActivity().getString(R.string.privacy_center_search_history_message_failed_delete_all)
            } else {
                requireActivity().getString(R.string.privacy_center_search_history_message_failed_delete_one)
            }
        showToasterError(message)
    }

    private fun onLoading() {
        binding?.apply {
            loaderUnify.show()
            localLoad.hide()
            textTitleListSearchHistory.hide()
            textDeleteAll.hide()
            rvHistorySearch.hide()
            imgListEmpty.hide()
            textListEmpty.hide()
        }
    }

    private fun onEmpty() {
        binding?.apply {
            loaderUnify.hide()
            localLoad.hide()
            textTitleListSearchHistory.hide()
            textDeleteAll.hide()
            rvHistorySearch.hide()
            imgListEmpty.show()
            textListEmpty.show()
        }

        binding?.imgListEmpty?.loadImageWithoutPlaceholder(
            requireActivity().getString(R.string.search_history_empty_image)
        )
    }

    private fun showListSearchHistory() {
        binding?.apply {
            loaderUnify.hide()
            localLoad.hide()
            textTitleListSearchHistory.show()
            textDeleteAll.show()
            rvHistorySearch.show()
            imgListEmpty.hide()
            textListEmpty.hide()
        }
    }

    private fun onFailed(throwable: Throwable) {
        binding?.apply {
            loaderUnify.hide()
            localLoad.show()
            textTitleListSearchHistory.hide()
            textDeleteAll.hide()
            rvHistorySearch.hide()
            imgListEmpty.hide()
            textListEmpty.hide()
        }

        binding?.localLoad?.apply {
            localLoadTitle = requireActivity().getString(R.string.privacy_center_search_history_local_load_title)
            localLoadDescription =
                requireActivity().getString(R.string.privacy_center_search_history_local_load_subtitle)
        }

        val message = throwable.getMessage(requireActivity())
        showToasterError(message)
    }

    private fun showDialogDeleteAll() {
        DialogUnify(requireActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.privacy_center_search_history_dialog_title))
            setDescription(getString(R.string.privacy_center_search_history_dialog_subtitle))
            setPrimaryCTAText(getString(R.string.privacy_center_search_history_dialog_primary))
            setSecondaryCTAText(getString(R.string.privacy_center_search_history_dialog_secondary))

            setPrimaryCTAClickListener {
                dismiss()
                loaderDeleteAllSearchHistory = LoaderDialog(requireActivity())
                loaderDeleteAllSearchHistory?.apply {
                    setLoadingText("")
                    dialog.setOverlayClose(false)
                    show()
                }
                viewModel.deleteSearchHistory(clearAll = true)
            }

            setSecondaryCTAClickListener {
                dismiss()
            }

            show()
        }
    }

    private fun setUpList(list: List<ItemSearch>) {
        if (list.isEmpty()) {
            onEmpty()
        } else {
            showListSearchHistory()
            adapter = SearchHistoryAdapter(list as MutableList<ItemSearch>) { itemSearch, position ->
                deleteOneSearchHistory(itemSearch, position)
            }
            binding?.rvHistorySearch?.adapter = adapter
        }
    }

    private fun deleteOneSearchHistory(itemSearch: ItemSearch, position: Int) {
        viewModel.deleteSearchHistory(position = position, clearAll = false, itemSearch = itemSearch)
    }

    private fun showToasterError(message: String) {
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    override fun getScreenName(): String =
        SearchHistoryFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(PrivacyCenterComponent::class.java).inject(this)
    }

    companion object {
        fun newInstance() = SearchHistoryFragment()
    }
}
