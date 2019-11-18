package com.tokopedia.imagesearch.domain.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory.ImageProductListTypeFactory;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilterModel implements Visitable<ImageProductListTypeFactory> {

    List<Item> itemList = new ArrayList<>();

    public CategoryFilterModel(List<Item> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
    }

    public List<Item> getItemList() {
        return itemList;
    }

    @Override
    public int type(ImageProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public static class Item {
        String name;
        String categoryId;

        public Item(String name, String categoryId) {
            this.name = name;
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public String getCategoryId() {
            return categoryId;
        }
    }
}
