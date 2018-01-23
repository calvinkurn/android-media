package com.tokopedia.tkpd.beranda.presentation.view.viewmodel;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 1/12/18.
 */

public class InspirationViewModel implements Visitable<HomeTypeFactory> {

    private String title;
    protected ArrayList<InspirationProductViewModel> listProduct;
    private int rowNumber;
    private String source;
    private int positionFeedCard;
    private String eventLabel;

    public InspirationViewModel(String title,
                                ArrayList<InspirationProductViewModel> listProduct,
                                String source) {
        this.title = title;
        this.listProduct = listProduct;
        this.source = source;
    }

    @Override
    public int type(HomeTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public ArrayList<InspirationProductViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<InspirationProductViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Object> getListProductAsObjectDataLayer(String eventLabel, String userId, int positionFeedCard) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < getListProduct().size(); i++) {
            InspirationProductViewModel viewModel = getListProduct().get(i);
            list.add(
                    DataLayer.mapOf(
                            "name", viewModel.getName(),
                            "id", viewModel.getProductId(),
                            "price", viewModel.getPriceInt(),
                            "brand", "",
                            "category", "",
                            "variant", "",
                            "list", String.format("/feed - product %d - %s", positionFeedCard, eventLabel),
                            "position", i,
                            "userId", userId
                    )
            );
        }
        return list;
    }

    public void setPositionFeedCard(int positionFeedCard) {
        this.positionFeedCard = positionFeedCard;
    }

    public int getPositionFeedCard() {
        return positionFeedCard;
    }

    public void setEventLabel(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public String getEventLabel() {
        return eventLabel;
    }
}
