package com.tokopedia.events.view.mapper;

import com.tokopedia.events.data.entity.response.Package;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.domain.model.ScheduleDomain;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranaymohapatra on 27/11/17.
 */

public class EventDetailsViewModelMapper {
    public static void mapDomainToViewModel(EventDetailsDomain source, EventsDetailsViewModel target) {
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setConvenienceFee(source.getConvenienceFee());
        target.setDateRange(source.getDateRange());
        target.setDuration(source.getDuration());
        target.setGenre(source.getGenre());
        target.setHasSeatLayout(source.getHasSeatLayout());
        target.setImageApp(source.getImageApp());
        target.setIsFeatured(source.getIsFeatured());
        target.setIsFoodAvailable(source.getIsFoodAvailable());
        target.setLongRichDesc(source.getLongRichDesc());
        target.setMrp(source.getMrp());
        target.setOfferText(source.getOfferText());
        target.setPromotionText(source.getPromotionText());
        target.setRating(source.getRating());
        target.setSalesPrice(source.getSalesPrice());
        target.setSeatChartTypeId(source.getSeatChartTypeId());
        target.setTnc(source.getTnc());
        target.setDisplayTags(source.getDisplayTags());
        target.setUrl(source.getUrl());
        target.setSeoUrl(source.getSeoUrl());
        target.setThumbnailApp(source.getThumbnailApp());
        target.setThumbsDown(source.getThumbsDown());
        target.setThumbsUp(source.getThumbsUp());
        target.setSeatMapImage(source.getSeatMapImage());
        target.setForms(source.getForms());
        target.setCityName(source.getCityName());
        target.setCustomText1(source.getCustomText1());
        String dateRange = "";
        if (source.getSaleStartDate() == source.getSaleEndDate()) {
            dateRange = Utils.getSingletonInstance().convertEpochToString(source.getSaleStartDate());
        } else {
            dateRange = Utils.getSingletonInstance().convertEpochToString(source.getSaleStartDate())
                        + " - " + Utils.getSingletonInstance().convertEpochToString(source.getSaleEndDate());
        }
        target.setTimeRange(dateRange);
        if (source.getSchedules() != null && source.getSchedules().size() > 0) {
            int size = source.getSchedules().size();
            target.setAddress(source.getSchedules().get(0).getAddressDetail().getCity());
            List<SchedulesViewModel> schedules = new ArrayList<>(size);
            for (ScheduleDomain item : source.getSchedules()) {
                SchedulesViewModel schedulesViewModel = new SchedulesViewModel();
                schedulesViewModel.setaDdress(item.getAddressDetail().getAddress());
                schedulesViewModel.setStartDate(item.getSchedule().getStartDate());
                schedulesViewModel.setEndDate(item.getSchedule().getEndDate());
                schedulesViewModel.setCityName(item.getAddressDetail().getCity());
                String timerange;
                if (source.getMinStartDate() > 0) {
                    if (item.getSchedule().getStartDate() == item.getSchedule().getEndDate()) {
                        timerange = Utils.getSingletonInstance().convertEpochToString(item.getSchedule().getStartDate());
                    } else {
                        timerange = Utils.getSingletonInstance().convertEpochToString(item.getSchedule().getStartDate())
                                + " - " + Utils.getSingletonInstance().convertEpochToString(item.getSchedule().getEndDate());
                    }
                } else {
                    timerange = "";
                }
                schedulesViewModel.setTimeRange(timerange);

                if (item.getGroups() != null
                        && !item.getGroups().isEmpty()
                        && item.getGroups().get(0) != null
                        && item.getGroups().get(0).getPackages() != null
                        ) {
                    int _size = item.getGroups().size();
                    List<Package> packages = new ArrayList<>();
                    List<Package> pack;
                    for (int i = 0; i < _size; i++) {
                        pack = item.getGroups().get(i).getPackages();
                        packages.addAll(pack);
                        pack.clear();
                    }
                    int numOfPkgs = packages.size();
                    List<PackageViewModel> packageViewModels = new ArrayList<>(numOfPkgs);
                    for (int j = 0; j < packages.size(); j++) {
                        PackageViewModel packageViewModel = new PackageViewModel();
                        Package aPackage = packages.get(j);
                        packageViewModel.setDigitalCategoryID(source.getCatalog().getDigitalCategoryId());
                        packageViewModel.setDigitalProductCode(source.getCatalog().getDigitalProductCode());
                        packageViewModel.setDigitalProductID(source.getCatalog().getDigitalProductId());
                        packageViewModel.setId(aPackage.getId());
                        packageViewModel.setProductId(aPackage.getProductId());
                        packageViewModel.setProductGroupId(aPackage.getProductGroupId());
                        packageViewModel.setProductScheduleId(aPackage.getProductScheduleId());
                        packageViewModel.setProviderScheduleId(aPackage.getProviderScheduleId());
                        packageViewModel.setDescription(aPackage.getDescription());
                        packageViewModel.setDisplayName(aPackage.getDisplayName());
                        packageViewModel.setAvailable(aPackage.getAvailable());
                        packageViewModel.setBooked(aPackage.getBooked());
                        packageViewModel.setTnc(aPackage.getTnc());
                        packageViewModel.setCommission(aPackage.getCommission());
                        packageViewModel.setCommissionType(aPackage.getCommissionType());
                        packageViewModel.setMaxQty(aPackage.getMaxQty());
                        packageViewModel.setMinQty(aPackage.getMinQty());
                        packageViewModel.setMrp(aPackage.getMrp());
                        packageViewModel.setProviderStatus(aPackage.getProviderStatus());
                        packageViewModel.setDisplayName(aPackage.getDisplayName());
                        packageViewModel.setSalesPrice(aPackage.getSalesPrice());
                        packageViewModel.setSold(aPackage.getSold());
                        packageViewModel.setConvenienceFee(aPackage.getConvenienceFee());
                        packageViewModel.setTimeRange(timerange);
                        packageViewModel.setThumbnailApp(target.getThumbnailApp());
                        packageViewModel.setAddress(schedulesViewModel.getaDdress());
                        packageViewModel.setFetchSectionUrl(aPackage.getFetchSectionUrl());
                        packageViewModel.setStartDate(aPackage.getStartDate());
                        packageViewModel.setEndDate(aPackage.getEndDate());
                        try {
                            packageViewModel.setForms(target.getForms());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        packageViewModel.setCategoryId(source.getCategoryId());
                        packageViewModel.setTitle(source.getTitle());
                        packageViewModels.add(packageViewModel);
                    }

                    schedulesViewModel.setPackages(packageViewModels);
                }
                schedules.add(schedulesViewModel);

            }
            target.setSchedulesViewModels(schedules);
        }
    }
}
