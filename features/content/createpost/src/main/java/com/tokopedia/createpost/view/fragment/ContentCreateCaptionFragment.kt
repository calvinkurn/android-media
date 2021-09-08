package com.tokopedia.createpost.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.view.adapter.CaptionPagePreviewImageAdapter
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import kotlinx.android.synthetic.main.content_caption_page_preview.*

class ContentCreateCaptionFragment : BaseCreatePostFragmentNew() {

    private val adapter: CaptionPagePreviewImageAdapter by lazy {
        CaptionPagePreviewImageAdapter()
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
            .createPostModule(CreatePostModule(requireContext().applicationContext))
            .build()
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
        initVar()
        initView()
    }

    private fun initVar() {
        createPostModel = createContentPostViewModel.getPostData() ?: CreatePostViewModel()
    }
    private fun initView() {
        adapter.updateProduct(createPostModel.completeImageList)
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

        caption.afterTextChanged {
            createPostModel.caption = it
        }
        caption.setOnTouchListener { v, event ->
            if (v.id == R.id.caption) {
                showKeyboard()
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
    }

    override fun onPause() {
        if (caption.isFocused)
            caption.clearFocus()
        super.onPause()
    }

    private fun showKeyboard() {
        activity?.let {
            (it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }
    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

}