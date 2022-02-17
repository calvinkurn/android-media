package com.tokopedia.play.broadcaster.view.fragment.loading

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.LoaderUnify

/**
 * Created by jegul on 29/06/20
 */
class LoadingDialogFragment : DialogFragment() {

    private lateinit var loader: LoaderUnify
    private var mLoaderType: LoaderType = LoaderType.CIRCULAR_WHITE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_loading, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    fun setLoaderType(loaderType: LoaderType) {
        mLoaderType = loaderType
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, LoadingDialogFragment::class.java.simpleName)
    }

    private fun setupView(view: View) {
        loader = view.findViewById(R.id.loader)
        loader.type = when(mLoaderType) {
            LoaderType.CIRCULAR -> LoaderUnify.TYPE_CIRCULAR
            LoaderType.CIRCULAR_WHITE -> LoaderUnify.TYPE_CIRCULAR_WHITE
        }
    }

    companion object {

        fun get(fragmentManager: FragmentManager): LoadingDialogFragment? {
            return fragmentManager.findFragmentByTag(LoadingDialogFragment::class.java.simpleName) as? LoadingDialogFragment
        }
    }

    enum class LoaderType {
        CIRCULAR, CIRCULAR_WHITE
    }
}