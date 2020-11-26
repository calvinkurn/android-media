package com.tokopedia.entertainment.pdp.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.entertainment.pdp.data.pdp.*

interface EventPDPFactory : AdapterTypeFactory{
    fun type(dataModel: EventPDPHighlightEntity) : Int
    fun type(dataModel: EventPDPAboutEntity) : Int
    fun type(dataModel: EventPDPFacilitiesEntity) : Int
    fun type(dataModel: EventPDPLocationDetailEntity) : Int
    fun type(dataModel: EventPDPInformationEntity) : Int
}