# GraphQL Networking library

Networking library for all graphql use-cases having below features

## Features

* Single and multifunction usecase for all type of graphql request
* Multiple query handling in one go.
* Added various level of enhance caching support (Plug and play based)
* Built in dagger implementation, hence no need to deepdive into dagger.
* It will be completely wipe-out the data & domain layer for graphql type request, hence user can more focus presentation layer.
* Easy to maintain raw query and its variable

## How it's work

* Add below GraphqlModule.class module into your component class like below

```
@Component(modules = {TokoCashModule.class, GraphqlModule.class}, dependencies = BaseAppComponent.class)

```

* And use graphql library into your project like below(Taken working code snip from my codebase please create all resource before run it)
  
```
public void testGraphqlUseCase() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("promoCode", "HCK2YPGHC5XJ");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CommonConstant.GqlApiKeys.QUERY, GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.gql_tokopoint_apply_coupon));
        requestParams.putObject(CommonConstant.GqlApiKeys.VARIABLES, variables);

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.gql_tokopoint_apply_coupon), ApplyCouponEntity.class, variables);

        GraphqlRequest graphqlRequest2 = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.gql_tokopoint_detail), TokoPointDetailEntity.class);

        GraphqlCacheStrategy graphqlCacheStrategy = new GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.val())
                .setSessionIncluded(true)
                .build();
        mGraphqlUseCase.setCacheStrategy(graphqlCacheStrategy);

        mGraphqlUseCase.setRequest(graphqlRequest);
        mGraphqlUseCase.setRequest(graphqlRequest2);

        mGraphqlUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("Graphql Lib", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                ApplyCouponEntity applyCouponEntity = objects.getData(ApplyCouponEntity.class);
                Log.d("Graphql Lib", applyCouponEntity.toString());

                TokoPointDetailEntity data = objects.getData(TokoPointDetailEntity.class);
                Log.d("Graphql Lib", data.toString());
            }
        });

    }

```


