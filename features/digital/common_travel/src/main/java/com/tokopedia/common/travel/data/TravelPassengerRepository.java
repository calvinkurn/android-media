package com.tokopedia.common.travel.data;

import com.tokopedia.common.travel.data.entity.TravelPassengerEntity;
import com.tokopedia.common.travel.database.TravelPassengerDao;
import com.tokopedia.common.travel.database.TravelPassengerTable;
import com.tokopedia.common.travel.domain.ITravelPassengerRepository;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 17/12/18.
 */
public class TravelPassengerRepository implements ITravelPassengerRepository {

    private TravelPassengerDao travelPassengerDao;
    private TravelPassengerMapper mapper;

    @Inject
    public TravelPassengerRepository(TravelPassengerDao travelPassengerDao, TravelPassengerMapper mapper) {
        this.travelPassengerDao = travelPassengerDao;
        this.mapper = mapper;
    }

    @Override
    public Observable<Boolean> deletePassenger(String idPassenger) {
        return Observable.just(travelPassengerDao.deletePassengerById(idPassenger) == 1);
    }

    @Override
    public Observable<List<TravelPassenger>> findAllTravelPassenger(List<TravelPassengerEntity> travelPassengerEntities,
                                                                    boolean resetDb) {
        return Observable.just(resetDb)
                .flatMap(new Func1<Boolean, Observable<List<TravelPassenger>>>() {
                    @Override
                    public Observable<List<TravelPassenger>> call(Boolean isReset) {
                        if (isReset) {
                            return Observable.just(travelPassengerDao.deleteAll())
                                    .map(integer -> mapper.transformEntity(travelPassengerEntities))
                                    .map(travelPassengerTables -> travelPassengerDao.insertAll(travelPassengerTables))
                                    .map(longs -> travelPassengerDao.findAllTravelPassenger())
                                    .map(travelPassengerTables -> mapper.transformTable(travelPassengerTables));
                        } else {
                            return Observable.from(travelPassengerEntities)
                                    .map(travelPassengerEntity -> {
                                        TravelPassengerTable passengerEntityTable = mapper
                                                .transform(travelPassengerEntity);

                                        TravelPassengerTable passengerDbTable = travelPassengerDao
                                                .findTravelPassengerByName(travelPassengerEntity.getName());
                                        if (passengerDbTable != null) {
                                            passengerEntityTable.setIdPassenger(passengerDbTable.getIdPassenger());
                                            passengerEntityTable.setSelected(passengerDbTable.getSelected());
                                            travelPassengerDao.update(passengerEntityTable);
                                        } else {
                                            travelPassengerDao.insert(passengerEntityTable);
                                        }
                                        return true;
                                    })
                                    .toList()
                                    .map(integers -> travelPassengerDao.findAllTravelPassenger())
                                    .map(travelPassengerTables -> mapper.transformTable(travelPassengerTables));
                        }
                    }
                });
    }

    @Override
    public Observable<Boolean> updatePassenger(boolean isSelected, String idPassenger) {
        return Observable.just(idPassenger)
                .map(idPassengerString -> {
                    return travelPassengerDao.updateTravelPassenger(isSelected ? 1 : 0, idPassenger) == 1;
                });
    }

    @Override
    public Observable<Boolean> updateDataTravelPassenger(String idPassenger, TravelPassengerEntity travelPassengerEntity) {
        return Observable.just(travelPassengerEntity)
                .flatMap(passengerEntity -> {
                    TravelPassengerTable passengerEntityTable = mapper
                            .transform(passengerEntity);

                    TravelPassengerTable passengerDbTable = travelPassengerDao
                            .findTravelPassengerByIdPassenger(idPassenger);
                    if (passengerDbTable != null) {
                        passengerEntityTable.setIdPassenger(passengerDbTable.getIdPassenger());
                        passengerEntityTable.setSelected(passengerDbTable.getSelected());
                        travelPassengerDao.update(passengerEntityTable);
                    }
                    return Observable.just(true);
                });
    }
}
