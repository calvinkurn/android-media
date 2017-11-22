import { PosCacheModule, ProductDiscoveryModule } from 'NativeModules'


// ===================== Product List ======================= //
export const FETCH_PRODUCTS = 'FETCH_PRODUCTS'
export const fetchProducts = (shopId, start, rows, etalaseId, productId, queryText) => {
  return {
    type: FETCH_PRODUCTS,
    payload: PosCacheModule.getDataAll("PRODUCT")
      .then(response => {
        const jsonResponse = JSON.parse(response)
        return jsonResponse;
      })
      .catch(error => {})
  }
}



export const FETCH_ETALASE = 'FETCH_ETALASE'
export const fetchEtalase = (shopId) => ({
  type: FETCH_ETALASE,
  payload: PosCacheModule.getDataAll("ETALASE")
    .then(res => {
      const jsonResponse = JSON.parse(res)
      return jsonResponse
    })
    .catch(err => console.log(err))
})


export const PULL_TO_REFRESH = 'PULL_TO_REFRESH'
export const pullToRefresh = () => {
  return {
    type: PULL_TO_REFRESH
  }
}


export const ON_ETALASE_CHANGE = 'ON_ETALASE_CHANGE'
export const onEtalaseChange = (id) => {
  return {
    type: ON_ETALASE_CHANGE,
    payload: id
  }
}


export const RESET_PRODUCT_LIST = 'RESET_PRODUCT_LIST'
export const resetProductList = () => {
  return {
    type: RESET_PRODUCT_LIST,
  }
}



// ==================== Search Product ==================== //
export const SEARCH_PRODUCT = 'SEARCH_PRODUCT'
export const searchProduct = (product, etalaseId) => {
  const payload = {
    keyword: product,
    etalase_id: etalaseId == 0 ? '' : etalaseId
  }

  return {
    type: SEARCH_PRODUCT,
    payload: searchProd(payload)
  }
}



// ==================== Search Product ==================== //
export const SEARCH_PRODUCT_SUBMIT = 'SEARCH_PRODUCT_SUBMIT'
export const searchProductSubmit = (product, etalaseId) => {
  const payload = {
    keyword: product,
    etalase_id: etalaseId == 0 ? '' : etalaseId
  }

  return {
    type: SEARCH_PRODUCT_SUBMIT,
    payload: searchProd(payload)
  }
}

const searchProd = (payload) => {
  return ProductDiscoveryModule.search(JSON.stringify(payload))
    .then(res => {
      const resJson = JSON.parse(res)
      return resJson
    })
    .catch(err => {
      console.log(err)
    })
}



// ==================== To Show the Query in The TextInput ==================== //
export const ON_SEARCH_QUERY_TYPE = 'ON_SEARCH_QUERY_TYPE'
export const onSearchQueryType = (queryText) => {
  return {
    type: ON_SEARCH_QUERY_TYPE,
    payload: queryText
  }
}



export const CLEAR_SEARCH_RESULTS = 'CLEAR_SEARCH_RESULTS'
export const clearSearchResults = () => {
  return {
    type: CLEAR_SEARCH_RESULTS,
  }
}

export const SET_SEARCH_TEXT = 'SET_SEARCH_TEXT'
export const setSearchText = (q) => {
  return {
    type: SET_SEARCH_TEXT,
    payload: q,
  }
}
