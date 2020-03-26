package com.tokopedia.play.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.tokopedia.play.R
import com.tokopedia.resources.common.R as commonResourceR

/**
 * Created by jegul on 16/03/20
 */
class PlayLoadingDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): PlayLoadingDialogFragment {
            return PlayLoadingDialogFragment()
        }
    }

    private lateinit var ivLoading: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_play_loading, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupLoading()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, PlayLoadingDialogFragment::class.java.simpleName)
    }

    private fun setupView(view: View) {
        ivLoading = view.findViewById(R.id.iv_loading)
    }

    private fun setupLoading() {
        if (ivLoading.drawable == null) {
            Glide.with(ivLoading.context)
                    .asGif()
                    .load(commonResourceR.drawable.ic_loading_indeterminate)
                    .into(ivLoading)
        }
    }
}