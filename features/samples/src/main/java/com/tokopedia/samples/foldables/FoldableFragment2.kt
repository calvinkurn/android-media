package com.tokopedia.samples.foldables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tokopedia.samples.R

class FoldableFragment2() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_container_foldable_2, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonNumber = arguments?.getString("buttonNumber") ?: ""
        view?.apply {
            findViewById<TextView>(R.id.button_text).text = buttonNumber
        }
    }

    companion object {
        fun getInstance(buttonNumber: String): FoldableFragment2 {
            val bundle = Bundle()
            bundle.putString("buttonNumber", buttonNumber)
            val fragment = FoldableFragment2()
            fragment.arguments = bundle
            return fragment
        }
    }
}
