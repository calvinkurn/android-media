package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetLocationListRequestUseCase;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.library.baseadapter.BaseAdapter;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Subscriber;

public class DealsPopularLocationAdapter extends BaseAdapter<Location> {

    private Context context;
    private SelectPopularLocationListener actionListener;
    private String url = "";
    private GetLocationListRequestUseCase getSearchLocationListRequestUseCase;
    private String searchText = "";
    private Location location;
    private AdapterCallback callback;
    private List<Location> mPopularLocations = new ArrayList<>();
    private boolean isForFirstTime = false;

    public DealsPopularLocationAdapter(Context context, SelectPopularLocationListener selectPopularLocationListener, AdapterCallback callback, Location location, boolean isForFirstTime) {
        super(callback);
        this.context = context;
        this.actionListener = selectPopularLocationListener;
        this.callback = callback;
        this.location = location;
        this.isForFirstTime = isForFirstTime;
        getSearchLocationListRequestUseCase = new GetLocationListRequestUseCase();
    }

    @Override
    public void loadData(int pageNumber) {
        super.loadData(pageNumber);
        RequestParams requestParams = RequestParams.create();
        if (pageNumber > 1) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            requestParams.putString("url", url);
        } else if (pageNumber == 1 && !TextUtils.isEmpty(searchText)) {
            requestParams.putString("name", searchText);
        } else {
            requestParams.putString("url", "");
            if (location.getCityId() == 0) {
                requestParams.putInt(Utils.LOCATION_CITY_ID, location.getId());
            } else {
                requestParams.putInt(Utils.LOCATION_CITY_ID, location.getCityId());
            }
        }
        getSearchLocationListRequestUseCase.setRequestParams(requestParams);
        getSearchLocationListRequestUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                loadCompletedWithError();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<LocationResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                LocationResponse locationResponse = (LocationResponse) dataResponse.getData();
                if (pageNumber == 1 && locationResponse.getLocations() != null && locationResponse.getLocations().size() > 0) {
                    clear();
                }
                if (isForFirstTime) {
                    mPopularLocations.addAll(locationResponse.getLocations());
                    isForFirstTime = false;
                }
                if (locationResponse.getLocations() == null) {
                    setLastPage(true);
                    clear();
                    addAll(mPopularLocations);
                    loadCompleted(mPopularLocations, locationResponse);
                    callback.onEmptyList(mPopularLocations);
                } else {
                    loadCompleted(locationResponse.getLocations(), locationResponse);
                    if (locationResponse.getPage() == null || !URLUtil.isValidUrl(locationResponse.getPage().getUriNext())) {
                        setLastPage(true);
                        url = "";
                    } else {
                        url = locationResponse.getPage().getUriNext();
                    }
                }

            }
        });

    }

    @Override
    protected BaseVH getItemViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(com.tokopedia.digital_deals.R.layout.popular_location_item, parent, false);
        return new DealsPopularLocationAdapter.ItemViewHolder(itemView);

    }

    public void setSearchText(String text) {
        this.searchText = text;
    }

    private class ItemViewHolder extends BaseVH implements View.OnClickListener {

        private int index;
        private TextView locationName, locAddress, locType;
        private ImageView locImage;
        private View itemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            locationName = itemView.findViewById(com.tokopedia.digital_deals.R.id.location_name);
            locAddress = itemView.findViewById(com.tokopedia.digital_deals.R.id.location_address);
            locType = itemView.findViewById(com.tokopedia.digital_deals.R.id.location_type);
            locImage = itemView.findViewById(com.tokopedia.digital_deals.R.id.popular_loc_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bindView(Location item, int position) {
            if (isForFirstTime) {
                for (Location location : mPopularLocations) {
                    bindData(location, position);
                }
            } else {
                bindData(item, position);
            }

        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        void bindData(Location location, int position) {
            this.index = position;
            if (!TextUtils.isEmpty(searchText)) {
                int startIndex = indexOfSearchQuery(location.getName(), searchText);
                if (startIndex == -1) {
                    locationName.setText(location.getName());
                } else {
                    SpannableString highlightedTitle = new SpannableString(location.getName());
                    highlightedTitle.setSpan(new TextAppearanceSpan(itemView.getContext(), com.tokopedia.digital_deals.R.style.searchTextHiglight),
                            0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    highlightedTitle.setSpan(new TextAppearanceSpan(itemView.getContext(), com.tokopedia.digital_deals.R.style.searchTextHiglight),
                            startIndex + searchText.length(), location.getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    locationName.setText(highlightedTitle);
                }
            } else {
                locationName.setText(location.getName());
            }
            if (!TextUtils.isEmpty(location.getAddress())) {
                locAddress.setVisibility(View.VISIBLE);
                locAddress.setText(location.getAddress());
            } else {
                locAddress.setVisibility(View.GONE);
            }

            if (location.getLocType() != null && !TextUtils.isEmpty(location.getLocType().getName())) {
                locType.setVisibility(View.VISIBLE);
                locType.setText(location.getLocType().getDisplayName());
                locType.setBackground(context.getResources().getDrawable(com.tokopedia.digital_deals.R.drawable.rect_grey_loc_type_background));
                ImageHandler.loadImage(context, locImage, location.getLocType().getIcon(), com.tokopedia.design.R.color.grey_1100, com.tokopedia.design.R.color.grey_1100);
            }

        }

        private int indexOfSearchQuery(String displayName, String searchTerm) {
            if (!TextUtils.isEmpty(searchTerm)) {
                return displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()));
            }
            return -1;
        }

        @Override
        public void onClick(View v) {
            Utils.getSingletonInstance().updateLocation(context, getItems().get(getIndex()));
            Location location = getItems().get(getIndex());
            actionListener.onPopularLocationSelected(location != null);
        }
    }

    public interface SelectPopularLocationListener {
        void onPopularLocationSelected(boolean locationUpdated);
    }
}
