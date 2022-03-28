package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.reviewcommon.databinding.FragmentReviewMediaGalleryLoadStateBinding

class ReviewMediaGalleryLoadStateFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentReviewMediaGalleryLoadStateBinding.inflate(
            inflater,
            container,
            false
        ).root
    }
}