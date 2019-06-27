
package com.tokopedia.trackingoptimizer.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tokopedia.trackingoptimizer.db.model.TrackingRegularDbModel.Companion.TRACKING_REGULAR_TABLE_NAME

/**
 * Created by hendry on 21/12/18.
 */
@Entity(tableName = TRACKING_REGULAR_TABLE_NAME)
class TrackingRegularDbModel: TrackingDbModel() {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    companion object {
        const val TRACKING_REGULAR_TABLE_NAME = "tracking_regular_tb"
    }
}