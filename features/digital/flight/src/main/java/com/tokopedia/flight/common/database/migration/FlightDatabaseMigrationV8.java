package com.tokopedia.flight.common.database.migration;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;
import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb;

@Migration(version = 8, database = TkpdFlightDatabase.class)
public class FlightDatabaseMigrationV8 extends BaseMigration {
    @Override
    public void migrate(DatabaseWrapper database) {
        database.execSQL("DROP TABLE IF EXISTS " + FlightAirportDB.class.getSimpleName());
        database.execSQL("DROP TABLE IF EXISTS " + FlightAirlineDB.class.getSimpleName());
        database.execSQL("DROP TABLE IF EXISTS " + FlightPassengerDb.class.getSimpleName());
        database.execSQL(new FlightAirportDB().getModelAdapter().getCreationQuery());
        database.execSQL(new FlightAirlineDB().getModelAdapter().getCreationQuery());
        database.execSQL(new FlightPassengerDb().getModelAdapter().getCreationQuery());
    }
}
