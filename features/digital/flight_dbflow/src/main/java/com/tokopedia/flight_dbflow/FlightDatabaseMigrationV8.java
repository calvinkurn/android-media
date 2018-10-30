package com.tokopedia.flight_dbflow;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by Rizky on 25/10/18.
 */
@Migration(version = 8, database = TkpdFlightDatabase.class)
public class FlightDatabaseMigrationV8 extends BaseMigration {

    @Override
    public void migrate(DatabaseWrapper database) {
        database.execSQL("DROP TABLE IF EXISTS " + FlightAirportDB.class.getSimpleName());
        database.execSQL("DROP TABLE IF EXISTS " + FlightAirlineDB.class.getSimpleName());
        database.execSQL("DROP TABLE IF EXISTS " + FlightPassengerDB.class.getSimpleName());
        database.execSQL(new FlightAirportDB().getModelAdapter().getCreationQuery());
        database.execSQL(new FlightAirlineDB().getModelAdapter().getCreationQuery());
        database.execSQL(new FlightPassengerDB().getModelAdapter().getCreationQuery());
    }

}
