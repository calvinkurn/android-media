import { PosCacheModule } from 'NativeModules'



//  ==================== Fetch Cart from Cache ===================== //
export const FETCH_CART_FROM_CACHE = 'FETCH_CART_FROM_CACHE'
export const fetchCartFromCache = () => {
  return {
    type: FETCH_CART_FROM_CACHE,
    payload: fetchCart()
  }
}

const fetchCart = () => {
  return PosCacheModule.getDataAll('CART')
    .then(response => {
      const jsonResponse = JSON.parse(response)
      return jsonResponse.data;
    })
    .catch(error => console.log(error))
}


//  ==================== Remove 1 item inside Cart ===================== //
export const REMOVE_FROM_CART = 'REMOVE_FROM_CART'
export const removeFromCart = (pid) => {
  return {
    type: REMOVE_FROM_CART,
    payload: PosCacheModule.deleteItem('CART', pid.toString())
      .then(response => {
        const jsonResponse = JSON.parse(response)
        console.log(response)
        if (jsonResponse.data.status) {
          return { pid }
        }
      })
      .catch(error => console.log(error))
  }
}


//  ==================== Increment Quantity inside Cart ===================== //
export const INCREMENT_QTY = 'INCREMENT_QTY'
export const incrementQty = (id, pid, qty) => {
  const quantity = qty + 1

  return {
    type: INCREMENT_QTY,
    payload: PosCacheModule.update('CART', `{id:${id}, product_id:${pid}, quantity:${quantity}}`)
      .then(response => {
        const jsonResponse = JSON.parse(response)
        if (jsonResponse.data.status) {
          return { id, pid, quantity }
        }
      })
      .catch(error => console.log(error))
  }
}


//  ==================== Decrement Quantity inside Cart ===================== //
export const DECREMENT_QTY = 'DECREMENT_QTY'
export const decrementQty = (id, pid, qty) => {
  const quantity = qty - 1

  return {
    type: DECREMENT_QTY,
    payload: PosCacheModule.update('CART', `{id:${id}, product_id:${pid}, quantity:${quantity}}`)
      .then(response => {
        const jsonResponse = JSON.parse(response)
        if (jsonResponse.data.status) {
          return { id, pid, quantity }
        }
      })
      .catch(error => console.log(error))
  }
}


//  ==================== Clear all Data inside Cart ===================== //
export const CLEAR_CART = 'CLEAR_CART'
export const clearCart = () => {
  return {
    type: CLEAR_CART,
    payload: PosCacheModule.deleteAll('CART')
      .then(response => { })
      .catch(error => console.log(error))
  }
}
