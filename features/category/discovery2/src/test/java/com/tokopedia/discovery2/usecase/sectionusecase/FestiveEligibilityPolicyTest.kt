package com.tokopedia.discovery2.usecase.sectionusecase

import com.tokopedia.discovery2.ComponentNames
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class FestiveEligibilityPolicyTest(
    private val isFeatureEnable: Boolean,
    private val isBackgroundAvailable: Boolean,
    private val childComponents: List<String?>,
    private val expected: Boolean
) {

    @Test
    fun `given `() {
        val objectUnderTest = FestiveEligibilityPolicy(isFeatureEnable, isBackgroundAvailable)
            .isAllowed(childComponents)

        assertEquals(expected, objectUnderTest)
    }

    companion object {

        private val unsupportedComponents: List<ComponentNames> by lazy {
            ComponentNames.values()
                .filter {
                    !FestiveEligibilityPolicy.componentsSupportBG.contains(it.componentName)
                }
        }

        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf(
                    false,
                    false,
                    listOf(
                        ComponentNames.LihatSemua.componentName,
                        ComponentNames.ProductCardCarousel.componentName
                    ),
                    false
                ),
                arrayOf(
                    false,
                    true,
                    listOf(
                        ComponentNames.LihatSemua.componentName,
                        ComponentNames.ProductCardCarousel.componentName
                    ),
                    false
                ),
                arrayOf(
                    true,
                    false,
                    listOf(
                        ComponentNames.LihatSemua.componentName,
                        ComponentNames.ProductCardCarousel.componentName
                    ),
                    false
                ),
                arrayOf(
                    true,
                    true,
                    listOf(
                        ComponentNames.LihatSemua.componentName,
                        unsupportedComponents.random().componentName,
                        ComponentNames.ProductCardCarousel.componentName,
                        ComponentNames.SingleBanner.componentName
                    ),
                    false
                ),
                arrayOf(
                    true,
                    true,
                    listOf(
                        ComponentNames.LihatSemua.componentName,
                        null,
                        ComponentNames.ProductCardCarousel.componentName
                    ),
                    false
                ),
                arrayOf(
                    true,
                    true,
                    listOf(
                        ComponentNames.LihatSemua.componentName,
                        ComponentNames.ProductCardCarousel.componentName,
                        ComponentNames.SingleBanner.componentName
                    ),
                    true
                )
            )
        }
    }
}
