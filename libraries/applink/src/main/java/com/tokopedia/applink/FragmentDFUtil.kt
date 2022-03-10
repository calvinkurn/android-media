package com.tokopedia.applink

import androidx.fragment.app.Fragment
import java.lang.Exception

/**
 * Fragment Dynamic Feature Utils
 */
object FragmentDFUtil {

    /**
     * Invoke public method from different module fragment
     */
    @JvmStatic
    fun invokeMethodThroughReflection(
            originFragment: Fragment?,
            originFragmentClassName: Class<*>?,
            targetedMethod: () -> Unit,
    ) {
        if (originFragment?.javaClass == originFragmentClassName) {
            try { targetedMethod() } catch (e: Exception) {}
        }
    }
}