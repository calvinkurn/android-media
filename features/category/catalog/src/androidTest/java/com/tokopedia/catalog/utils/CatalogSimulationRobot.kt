package com.tokopedia.catalog.utils


class CatalogSimulationRobot {

    infix fun assertTest(action: CatalogSimulationRobot.() -> Unit) = CatalogSimulationRobot().apply(action)
}

fun actionTest(action: CatalogSimulationRobot.() -> Unit) = CatalogSimulationRobot().apply(action)