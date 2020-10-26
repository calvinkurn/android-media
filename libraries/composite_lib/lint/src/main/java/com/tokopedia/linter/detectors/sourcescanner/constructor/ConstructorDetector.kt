package com.tokopedia.linter.detectors.sourcescanner.constructor

import com.android.SdkConstants
import com.android.tools.lint.detector.api.JavaContext
import com.intellij.psi.PsiMethod
import com.tokopedia.linter.unify.UnifyComponentsList
import com.tokopedia.linter.unify.UnifyDetector.checkUnify
import org.jetbrains.uast.UCallExpression

object ConstructorDetector {
    var applicableConstructorList = (UnifyComponentsList.widgetViewMapping.keys.map { SdkConstants.WIDGET_PKG_PREFIX + it }) +
            UnifyComponentsList.getUnifyMapping().keys

    fun checkConstructor(context: JavaContext,
                         node: UCallExpression,
                         constructor: PsiMethod) {
        val name = constructor.containingClass?.qualifiedName ?: return
        checkUnify(context, node, name)

    }
}