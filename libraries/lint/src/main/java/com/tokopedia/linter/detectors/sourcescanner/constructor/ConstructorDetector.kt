package com.tokopedia.linter.detectors.sourcescanner.constructor

import com.android.tools.lint.detector.api.JavaContext
import com.intellij.psi.PsiMethod
import com.tokopedia.linter.unify.UnifyComponentsList
import com.tokopedia.linter.unify.UnifyDetector.checkUnify
import org.jetbrains.uast.UCallExpression

object ConstructorDetector {
    var applicableConstructorList = UnifyComponentsList.getUnifyMapping().keys

    fun checkConstructor(context: JavaContext,
                         node: UCallExpression,
                         constructor: PsiMethod) {
        val name = constructor.containingClass?.qualifiedName ?: return
        System.out.println(name)
        checkUnify(context, node, name)

    }
}