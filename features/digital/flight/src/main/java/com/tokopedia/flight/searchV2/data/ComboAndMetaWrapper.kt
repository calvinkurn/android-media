package com.tokopedia.flight.searchV2.data

import com.tokopedia.flight.search.data.cloud.model.response.Meta
import com.tokopedia.flight.searchV2.data.db.FlightComboTable

/**
 * Created by Rizky on 10/10/18.
 */
class ComboAndMetaWrapper(val flightComboTables: List<FlightComboTable>,
                          val meta: Meta)