package com.tokopedia.createpost.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.view.adapter.CaptionPagePreviewImageAdapter
import com.tokopedia.createpost.view.util.widget.ClearFocusEditText
import com.tokopedia.imagepicker_insta.common.ui.menu.MenuManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

class ContentCreateCaptionFragment : BaseCreatePostFragmentNew() {

    private var captionTxt: ClearFocusEditText? = null
    private var contentPreviewRv : RecyclerView? = null
    private var adapter: CaptionPagePreviewImageAdapter? =
        CaptionPagePreviewImageAdapter(onItemClick = this::onItemClick)


    override fun fetchContentForm() {
        presenter.fetchContentForm(createPostModel.productIdList,
            createPostModel.authorType,
            createPostModel.postId)
    }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ContentCreateCaptionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initInjector() {
        DaggerCreatePostComponent.builder()
            .createPostCommonModule(CreatePostCommonModule(requireContext().applicationContext))
            .createPostModule(CreatePostModule(requireContext().applicationContext)).build()
            .inject(this)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.content_caption_page_preview, container, false)
        initialiseViews(v)
        return v
    }



    private fun initialiseViews(v: View) {
        captionTxt = v.findViewById(R.id.caption)
        contentPreviewRv = v.findViewById(R.id.content_post_image_rv)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireArguments().getParcelable<CreatePostViewModel>(CreatePostViewModel.TAG)?.let {
            createContentPostViewModel.setNewContentData(it)
            createPostModel = it
        }
        setHasOptionsMenu(true)
        initVar()
        initView()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuTitle = activity?.getString(com.tokopedia.content.common.R.string.feed_content_text_post)
        if (!menuTitle.isNullOrEmpty()) {
            MenuManager.addCustomMenu(activity, menuTitle, true, menu) {
                activityListener?.postFeed()
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
    private fun onItemClick(position: Int) {
        activityListener?.openProductTaggingPageOnPreviewMediaClick(position)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MenuManager.MENU_ITEM_ID -> {
                activityListener?.postFeed()
                return true
            }
        }
        return false
    }

    private fun initVar() {
        if (createContentPostViewModel.getPostData() != null)
        createPostModel = createContentPostViewModel.getPostData()!!
    }

    private fun initView() {
        adapter?.updateProduct(createPostModel.completeImageList)
        if (!createPostModel.isEditState)
            contentPreviewRv?.visible()
        else
            contentPreviewRv?.gone()
        contentPreviewRv?.setHasFixedSize(true)
        contentPreviewRv?.layoutManager =
            LinearLayoutManager(parentFragment?.context, LinearLayoutManager.HORIZONTAL, false)
        contentPreviewRv?.adapter = adapter

        updateCaption()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun updateCaption() {
        if (createPostModel.caption.isNotEmpty())
            captionTxt?.setText(createPostModel.caption)


        captionTxt?.afterTextChanged {
            createPostModel.caption = it
        }
        captionTxt?.setOnTouchListener { v, event ->
            if (v.id == R.id.caption) {
                showKeyboard()
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                    MotionEvent.ACTION_DOWN -> {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                        createPostAnalytics.eventClickOnCaptionBox()
                    }
                    MotionEvent.ACTION_MOVE -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
    }

    override fun onPause() {
        if (captionTxt?.isFocused == true)
            captionTxt?.clearFocus()
        hideKeyboard()
        super.onPause()
    }

    private fun showKeyboard() {
        activity?.let {
            (it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }
    private fun hideKeyboard() {
        KeyboardHandler.hideSoftKeyboard(requireActivity())
    }
    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    fun deleteAllProducts() {
        createPostModel.completeImageList.forEach {
            it.products.clear()
            it.tags.clear()
        }
        createContentPostViewModel.setNewContentData(createPostModel)
        adapter?.updateProduct(createPostModel.completeImageList)
    }
}
