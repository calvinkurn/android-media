
package com.tokopedia.trackingoptimizer.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel.Companion.TRACKING_EE_FULL_TABLE_NAME

/**
 * Created by hendry on 21/12/18.
 */
@Entity(tableName = TRACKING_EE_FULL_TABLE_NAME)
class TrackingEEFullDbModel: TrackingDbModel() {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var enhanceEcommerce: String = ""

    companion object {
        const val TRACKING_EE_FULL_TABLE_NAME = "tracking_ee_full_tb"
    }
}