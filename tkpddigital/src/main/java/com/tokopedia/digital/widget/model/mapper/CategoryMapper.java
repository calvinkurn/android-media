package com.tokopedia.digital.widget.model.mapper;

import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.widget.data.entity.category.CategoryEntity;
import com.tokopedia.digital.widget.model.category.Attributes;
import com.tokopedia.digital.widget.model.category.Category;
import com.tokopedia.digital.widget.model.category.ClientNumber;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/4/17.
 */

public class CategoryMapper implements Func1<List<CategoryEntity>, List<Category>> {

    @Override
    public List<Category> call(List<CategoryEntity> categoryEntities) throws MapperDataException {
        List<Category> categoryList = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            ClientNumber clientNumber = new ClientNumber();
            clientNumber.setHelp(categoryEntity.getAttributes().getClientNumber().getHelp());
            clientNumber.setOperatorStyle(categoryEntity.getAttributes().getClientNumber().getOperatorStyle());
            clientNumber.setPlaceholder(categoryEntity.getAttributes().getClientNumber().getPlaceholder());
            clientNumber.setShown(categoryEntity.getAttributes().getClientNumber().isShown());
            clientNumber.setText(categoryEntity.getAttributes().getClientNumber().getText());

            Attributes attributes = new Attributes();
            attributes.setClientNumber(clientNumber);
            attributes.setDefaultOperatorId(categoryEntity.getAttributes().getDefaultOperatorId());
            attributes.setIcon(categoryEntity.getAttributes().getIcon());


            Category category = new Category();
            category.setAttributes(attributes);
            category.setId(categoryEntity.getId());
            category.setType(categoryEntity.getType());

            categoryList.add(category);
        }
        return categoryList;
    }
}
