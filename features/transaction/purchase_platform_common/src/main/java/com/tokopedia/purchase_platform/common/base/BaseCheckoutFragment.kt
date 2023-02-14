package com.tokopedia.purchase_platform.common.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment

/**
 * @author anggaprasetiyo on 18/04/18.
 */
abstract class BaseCheckoutFragment : TkpdBaseV4Fragment() {

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        setHasOptionsMenu(getOptionsMenuEnable())
    }

    override fun getScreenName(): String {
        return this.javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    protected abstract fun initInjector()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getFragmentLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!userVisibleHint) {
            return
        }
    }

    /**
     * Layout xml untuk si fragment
     *
     * @return layout id
     */
    protected abstract fun getFragmentLayout(): Int

    /**
     * apakah fragment ini support options menu?
     *
     * @return iya atau tidak
     */
    protected abstract fun getOptionsMenuEnable(): Boolean

    /**
     * initial view atau widget.. misalkan textView = (TextView) findById...
     *
     * @param view root view si fragment
     */
    protected abstract fun initView(view: View)
}
