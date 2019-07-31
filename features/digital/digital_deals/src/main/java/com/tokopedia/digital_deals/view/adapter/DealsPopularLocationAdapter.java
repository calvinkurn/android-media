package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import com.tokopedia.digital_deals.R;
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
import java.util.Map;

import rx.Subscriber;

public class DealsPopularLocationAdapter extends BaseAdapter<Location> {

    private Context context;
    private List<Location> locations = new ArrayList<>();
    private SelectPopularLocationListener actionListener;
    private String url = "";
    private GetLocationListRequestUseCase getSearchLocationListRequestUseCase;
    private String searchText = "";

    public DealsPopularLocationAdapter(Context context, SelectPopularLocationListener selectPopularLocationListener, AdapterCallback callback) {
        super(callback);
        this.context = context;
        this.actionListener = selectPopularLocationListener;
        getSearchLocationListRequestUseCase = new GetLocationListRequestUseCase();
    }

    @Override
    public void loadData(int pageNumber) {
        super.loadData(pageNumber);
        RequestParams requestParams = RequestParams.create();
        if (pageNumber > 1) {
            requestParams.putString("url", url);
        } else if (pageNumber == 1 && !TextUtils.isEmpty(searchText)) {
            requestParams.putString("name", searchText);
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
                loadCompleted(locationResponse.getLocations(), locationResponse);
                if (locationResponse.getPage() == null || !URLUtil.isValidUrl(locationResponse.getPage().getUriNext())) {
                    setLastPage(true);
                } else {
                    url = locationResponse.getPage().getUriNext();
                }
            }
        });

    }

    @Override
    protected BaseVH getItemViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.popular_location_item, parent, false);
            return new DealsPopularLocationAdapter.ItemViewHolder(itemView);

    }

    public void setSearchText(String text) {
        this.searchText = text;
    }

    private class ItemViewHolder extends BaseVH implements View.OnClickListener{

        private int index;
        private TextView locationName, locAddress, locType;
        private ImageView locImage;
        private View itemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            locationName = itemView.findViewById(R.id.location_name);
            locAddress = itemView.findViewById(R.id.location_address);
            locType = itemView.findViewById(R.id.location_type);
            locImage = itemView.findViewById(R.id.popular_loc_img);
        }

        @Override
        public void bindView(Location item, int position) {
            bindData(item);
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        void bindData(Location location) {
            locationName.setText(location.getName());
            locAddress.setText(location.getAddress());
            if (!TextUtils.isEmpty(location.getLocType())) {
                locType.setVisibility(View.VISIBLE);
                locType.setText(location.getLocType());
                locType.setBackground(context.getResources().getDrawable(R.drawable.rect_grey_loc_type_background));
            }
            ImageHandler.loadImage(context, locImage, location.getImageApp(), R.color.grey_1100, R.color.grey_1100);
        }

        @Override
        public void onClick(View v) {
            Location location = Utils.getSingletonInstance().getLocation(context);
            Utils.getSingletonInstance().updateLocation(context, locations.get(getIndex()));
            actionListener.onPopularLocationSelected(location != null);
        }
    }

    public interface SelectPopularLocationListener {
        void onPopularLocationSelected(boolean locationUpdated);
    }
}
