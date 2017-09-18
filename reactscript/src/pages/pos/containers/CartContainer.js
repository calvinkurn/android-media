import React, { Component } from 'react'
import { connect } from 'react-redux'
import CartItemList from '../components/cart/CartItemList'
import {
  removeFromCart,
  incrementQty,
  decrementQty,
  clearCart,
  fetchCartFromCache
} from '../actions/index'

const mapStateToProps = (state, ownProps) => {
  return {
    items: state.cart.items,
    visible: ownProps.visible,
    onBackPress: ownProps.onBackPress,
    totalPrice: state.cart.totalPrice,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    fetchCartList: () => {
      dispatch(fetchCartFromCache())
    },
    onItemClick: (id) => {
    },
    onRemoveFromCart: (id) => {
      dispatch(removeFromCart(id))
    },
    onRemoveAllFromCart: () => {
      dispatch(clearCart())
    },
    onIncrQty: (id) => {
      dispatch(incrementQty(id))
    },
    onDecrQty: (id) => {
      dispatch(decrementQty(id))
    },

  }
}

const CartContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(CartItemList)

export default CartContainer
