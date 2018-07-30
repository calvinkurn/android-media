package com.tokopedia.navigation.data.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.navigation.data.entity.DrawerNotificationEntity;

import rx.functions.Func1;

/**
 * Created by meta on 25/07/18.
 */
public class DrawerNotificationMapper implements Func1<GraphqlResponse, DrawerNotificationEntity> {

    @Override
    public DrawerNotificationEntity call(GraphqlResponse graphqlResponse) {
        return graphqlResponse.getData(DrawerNotificationEntity.class);
    }
}
