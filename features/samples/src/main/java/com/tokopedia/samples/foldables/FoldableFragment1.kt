package com.tokopedia.samples.foldables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.samples.R

class FoldableFragment1(): Fragment(){
    var listener : Listener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_container_foldable_1, container, false)
        return view
    }

    fun setListenerValue(listener : Listener) {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            findViewById<View>(R.id.buttton1).setOnClickListener {
                listener?.onClick("1")
            }
            findViewById<View>(R.id.buttton2).setOnClickListener {
                listener?.onClick("2")
            }
            findViewById<View>(R.id.buttton3).setOnClickListener {
                listener?.onClick("3")
            }
            findViewById<View>(R.id.buttton4).setOnClickListener {
                listener?.onClick("4")
            }
            findViewById<View>(R.id.buttton5).setOnClickListener {
                listener?.onClick("5")
            }
        }
    }

    interface Listener {
        fun onClick(buttonNumber : String)
    }
}
