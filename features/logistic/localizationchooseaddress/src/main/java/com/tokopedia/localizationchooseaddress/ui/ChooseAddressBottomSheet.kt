package com.tokopedia.localizationchooseaddress.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.localizationchooseaddress.di.ChooseAddressComponent
import com.tokopedia.unifycomponents.BottomSheetUnify

class ChooseAddressBottomSheet: BottomSheetUnify(), HasComponent<ChooseAddressComponent> {

    private var fm: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getComponent(): ChooseAddressComponent {
        return activity?.run
    }

    fun show(fm: FragmentManager?) {
        this.fm = fm
        fm?.let {
            show(it, "")
        }
    }



}