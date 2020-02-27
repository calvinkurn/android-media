package com.tokopedia.travelhomepage.homepage.presentation.adapter.itemdecoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.travelhomepage.shouldBeEquals
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author by furqan on 04/02/2020
 */

class TravelHomepageDestinationViewDecoratorTest : Spek({
    Feature("Get View Decoration Between Destination Item") {
        val viewDecorator = TravelHomepageDestinationViewDecorator()

        Scenario("The First Item") {
            val recyclerView = mockk<RecyclerView>()
            val outRect = Rect()

            every { recyclerView.layoutManager!!.getPosition(any()) } returns 0

            When("get item offset") {
                viewDecorator.getItemOffsets(outRect, mockk(), recyclerView, mockk())
            }

            Then("left side should have 0 offset") {
                outRect.left shouldBeEquals 0
            }

            Then("right side should have 0 offset") {
                outRect.right shouldBeEquals 0
            }
        }

        Scenario("Item Position is Odd") {
            val recyclerView = mockk<RecyclerView>()
            val outRect = Rect()

            every { recyclerView.layoutManager!!.getPosition(any()) } returns 1

            When("get item offset") {
                viewDecorator.getItemOffsets(outRect, mockk(), recyclerView, mockk())
            }

            Then("right side should have 16 offset") {
                outRect.right shouldBeEquals 16
            }
        }

        Scenario("Item Position is Even") {
            val recyclerView = mockk<RecyclerView>()
            val outRect = Rect()

            every { recyclerView.layoutManager!!.getPosition(any()) } returns 2

            When("get item offset") {
                viewDecorator.getItemOffsets(outRect, mockk(), recyclerView, mockk())
            }

            Then("left side should have 16 offset") {
                outRect.left shouldBeEquals 16
            }
        }
    }
})