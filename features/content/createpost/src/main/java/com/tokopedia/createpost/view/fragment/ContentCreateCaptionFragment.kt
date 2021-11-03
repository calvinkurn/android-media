package com.tokopedia.createpost.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.view.adapter.CaptionPagePreviewImageAdapter
import com.tokopedia.imagepicker_insta.common.ui.menu.MenuManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.content_caption_page_preview.*

class ContentCreateCaptionFragment : BaseCreatePostFragmentNew() {

    private val adapter: CaptionPagePreviewImageAdapter by lazy {
        CaptionPagePreviewImageAdapter(listener = activityListener)
    }


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
        return inflater.inflate(R.layout.content_caption_page_preview, container, false)
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
        val menuTitle = activity?.getString(R.string.feed_content_text_post)
        if (!menuTitle.isNullOrEmpty()) {
            MenuManager.addCustomMenu(activity, menuTitle, true, menu) {
                activityListener?.postFeed()
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
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
        adapter.updateProduct(createPostModel.completeImageList)
        if (!createPostModel.isEditState)
            content_post_image_rv.visible()
        else
            content_post_image_rv.gone()
        content_post_image_rv.setHasFixedSize(true)
        content_post_image_rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        content_post_image_rv.adapter = adapter

        updateCaption()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun updateCaption() {
        if (createPostModel.caption.isNotEmpty())
            caption.setText(createPostModel.caption)
        if (createPostModel.caption.isEmpty())
            caption.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES


        caption.afterTextChanged {
            createPostModel.caption = it
        }
        caption.setOnTouchListener { v, event ->
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
        if (caption.isFocused)
            caption.clearFocus()
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

}