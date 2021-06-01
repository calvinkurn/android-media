package com.tokopedia.sellerfeedback.presentation.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerfeedback.R
import com.tokopedia.sellerfeedback.presentation.adapter.FeedbackPageAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import kotlinx.coroutines.*

class SellerFeedbackPageChooserBottomSheet(private val selectedTitle: String) : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_seller_feedback_page_chooser
        private val TAG: String? = SellerFeedbackPageChooserBottomSheet::class.java.canonicalName

        fun createInstance(selectedTitle: String): SellerFeedbackPageChooserBottomSheet {
            return SellerFeedbackPageChooserBottomSheet(selectedTitle).apply {
                clearContentPadding = true
                showCloseIcon = false
                showKnob = true
                isKeyboardOverlap = false
                isHideable = true
                isFullpage = true
            }
        }
    }

    private var listener: BottomSheetListener? = null

    private var searchBar: SearchBarUnify? = null
    private var rvListPage: RecyclerView? = null
    private var errorState: View? = null

    private var stringPages: List<String>? = null
    private var adapter: FeedbackPageAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflateLayout(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
        initList()
        setupInteraction()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, TAG)
    }

    private fun dismiss(manager: FragmentManager) {
        (manager.findFragmentByTag(TAG) as? BottomSheetUnify)
                ?.dismissAllowingStateLoss()
    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    private fun initView(view: View) {
        searchBar = view.findViewById(R.id.search_bar)
        errorState = view.findViewById(R.id.view_page_not_found)
        rvListPage = view.findViewById(R.id.rv_list_page)
    }

    private fun initList() {

        adapter = FeedbackPageAdapter { onItemClicked(it) }

        rvListPage?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SellerFeedbackPageChooserBottomSheet.adapter
        }

        stringPages = resources.getStringArray(R.array.feedback_pages).toList()

        val stringPages = stringPages ?: return
        adapter?.apply {
            setSelected(selectedTitle)
            updateList(stringPages)
        }
    }

    private fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(LAYOUT, container)
        val menuTitle = itemView.context.getString(R.string.seller_feedback_choose_page)
        setTitle(menuTitle)
        setChild(itemView)
    }

    private fun onItemClicked(title: String) {
        activity?.supportFragmentManager?.let {
            listener?.onPageSelected(title)
            dismiss(it)
        }
    }

    private fun setupInteraction() {
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val stringPages = stringPages ?: return
                val text = p0?.toString() ?: ""
                val filteredPages = stringPages.filter { it.contains(text, true) }
                adapter?.updateList(filteredPages)
                showErrorPageNotFound(filteredPages.isEmpty())
            }
        }
        searchBar?.searchBarTextField?.addTextChangedListener(searchTextWatcher)
    }

    private fun showErrorPageNotFound(isShow: Boolean) {
        errorState?.visibility = if (isShow) {
            View.VISIBLE
        } else View.GONE
    }

    fun interface BottomSheetListener {
        fun onPageSelected(title: String)
    }
}