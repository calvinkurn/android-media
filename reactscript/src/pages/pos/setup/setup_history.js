import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from '../store/Store'
import TransactionHistory from '../components/TransactionHistory'


class Root extends Component {
    render() {
        return (
            <Provider store={store}>
                <TransactionHistory onBackPress={() => {}}/>
            </Provider>
        )
    }
}
export default Root