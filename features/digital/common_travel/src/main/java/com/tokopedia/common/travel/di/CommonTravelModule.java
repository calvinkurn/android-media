package com.tokopedia.common.travel.di;

import com.tokopedia.common.travel.data.TravelPassengerDataStoreFactory;
import com.tokopedia.common.travel.data.TravelPassengerDbDataStore;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nabillasabbaha on 13/08/18.
 */
@Module
public class CommonTravelModule {

    @Provides
    TravelPassengerDbDataStore provideDbDataStore() {
        return new TravelPassengerDbDataStore();
    }

    @Provides
    TravelPassengerDataStoreFactory provideTravelPassengerDataStoreFactory(TravelPassengerDbDataStore travelPassengerDbDataStore) {
        return new TravelPassengerDataStoreFactory(travelPassengerDbDataStore);
    }

}
