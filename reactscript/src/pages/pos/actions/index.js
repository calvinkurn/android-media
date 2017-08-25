import { NetworkModule } from 'NativeModules'

const ACE_HOSTNAME = 'https://ace.tokopedia.com'
const TOME_HOSTNAME = 'https://tome.tokopedia.com'

export const FETCH_PRODUCTS = 'FETCH_PRODUCTS'
export const fetchProducts = (shopId, start, rows, etalaseId) => {
  const eId = +etalaseId || 0
  let url = `${ACE_HOSTNAME}/search/product/v3.1?device=android&source=shop_product&rows=${rows}&shop_id=${shopId}&start=${start}`
  console.log(url)
  if (eId) {
    url += `&etalase=${eId}`
  }

  const getProducts = () => {
    return NetworkModule.getResponse(url, "GET", "", false)
      .then(response => {
        const jsonResponse = JSON.parse(response)
        return {
           data: jsonResponse
        }
      })
      .catch(error => {
        return {
          data: []
        }
      })
  }

  return {
    type: FETCH_PRODUCTS,
    payload: getProducts()
  }
}

export const FETCH_ETALASE = 'FETCH_ETALASE'
export const fetchEtalase = (shopId) => {
  const url = `${TOME_HOSTNAME}/v1/web-service/shop/get_etalase?shop_id=${shopId}`

  const getEtalase = () => {
    return NetworkModule.getResponse(url, "GET", "", false)
      .then(response => {
        const jsonResponse = JSON.parse(response)
        return {
          data: jsonResponse
        }
      })
      .catch(error => {
        return {
          data: []
        }
      })
  }

  return {
    type: FETCH_ETALASE,
    payload: getEtalase()
  }
}

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
